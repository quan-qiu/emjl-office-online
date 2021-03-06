package net.csdcodes.service;

import com.opencsv.CSVReader;
import net.csdcodes.model.ProcurementRequisitionDetail;

import net.csdcodes.model.User;
import net.csdcodes.service.exception.StorageException;
import net.csdcodes.service.exception.StorageFileNotFoundException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {
    @Autowired
    ProcurementRequisitionService prs;

    @Autowired
    UserService userService;

    @Value("${app.upload.root-dir}")
    private String rootDir;
    @Value("${app.upload.file-root-dir}")
    private String location;
    @Value("${app.upload.pr-location-dir}")
    private String prLocation;
    @Value("${app.upload.prd-template-dir}")
    private String prdTemplateLocation;
    @Value("${app.upload.prd-upload-dir}")
    private String prdUploadLocation;

    public static int noOfQuickServiceThreads = 20;


    /**
     * this statement create a thread pool of twenty threads
     * here we are assigning send mail task using ScheduledExecutorService.submit();
     */
    private ScheduledExecutorService quickService = Executors.newScheduledThreadPool(noOfQuickServiceThreads); // Creates a thread pool that reuses fixed number of threads(as specified by noOfThreads in this case).


    @Override
    public void storeToDir(MultipartFile file, String dirType) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User thisUser = userService.getUserByUsername(auth.getName());

        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
        }
        switch (dirType) {
            case "PR_PRD_TEMPLATE":
                copyFileToDir(file, Paths.get(rootDir + prdTemplateLocation +
                         thisUser.getSsn().trim() + File.separator), "PRD_TEMPLATE.xlsx");
                break;
            case "PR_PRD_UPLOAD":
                copyFileToDir(file, Paths.get(rootDir + prdUploadLocation +
                        thisUser.getSsn().trim() + File.separator), null);
                break;
        }

    }


    public void copyFileToDir(MultipartFile file, Path destination, String fileName) {
        quickService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Path destinationFile;
                    if (fileName != null) {
                        destinationFile = destination.resolve(
                                Paths.get(fileName))
                                .normalize().toAbsolutePath();
                    } else {
                        destinationFile = destination.resolve(
                                Paths.get(file.getOriginalFilename()))
                                .normalize().toAbsolutePath();
                    }

                    File dir = new File(destination.toAbsolutePath().toString());

                    if (!dir.exists()) {
                        boolean created = dir.mkdirs();
                    }

                    if (!destinationFile.getParent().equals(destination.toAbsolutePath())) {
                        // This is a security check
                        throw new StorageException(
                                "Cannot store file outside current directory.");
                    }

                    try (InputStream inputStream = file.getInputStream()) {
                        File temp = new File(destinationFile.toString());

                        if (temp.exists())
                            temp.delete();

                        Files.copy(inputStream, destinationFile,
                                StandardCopyOption.REPLACE_EXISTING);
                        System.out.println(destinationFile.toString());
                        inputStream.close();
                    }
                    /*FileUtil fileUtil = new FileUtil();
                    fileUtil.copyFileToDestination(file,destinationFile);*/

                } catch (Exception ioe) {
                    throw new StorageException("Failed to store file " + file.getOriginalFilename(), ioe);
                }

            }});

        /*try {
            Path destinationFile;
            if (fileName != null) {
                destinationFile = destination.resolve(
                        Paths.get(fileName))
                        .normalize().toAbsolutePath();
            } else {
                destinationFile = destination.resolve(
                        Paths.get(file.getOriginalFilename()))
                        .normalize().toAbsolutePath();
            }

            File dir = new File(destination.toAbsolutePath().toString());

            if (!dir.exists()) {
                boolean created = dir.mkdirs();
            }

            if (!destinationFile.getParent().equals(destination.toAbsolutePath())) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
                System.out.println(destinationFile.toString());

                inputStream.close();
            }

        } catch (IOException ioe) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), ioe);
        }*/

    }

    public void loadPRDFromFile(String filename, int prmId , String userSsn) throws ParseException, IOException {

        quickService.submit(new Runnable() {
            @Override
            public void run() {
                Path destinationFile;
                Path prdUploadPath = Paths.get(rootDir + prdUploadLocation +
                        userSsn);
                System.out.println("loadPRDFromFile prdUploadPath : " + prdUploadPath.toString());

                BufferedReader reader = null;
                CSVReader csvReader = null;

                destinationFile = prdUploadPath.resolve(
                        Paths.get(filename))
                        .normalize().toAbsolutePath();
                System.out.println("loadPRDFromFile destinationFile : " + destinationFile.toString());
                try {
                    // create a reader
                    //Reader reader = Files.newBufferedReader(destinationFile);
                    reader = new BufferedReader((Reader) destinationFile);
                    // create csv reader
                    csvReader = new CSVReader(reader);

                    // read one record at a time
                    String[] record;
                    csvReader.readNext();
                    while ((record = csvReader.readNext()) != null) {
                        ProcurementRequisitionDetail prd = new ProcurementRequisitionDetail();
                        prd.setPrMainId(prmId);

                        //System.out.println("ERP_code: " + record[0]);
                        prd.setItemErpCode(record[0]);

                        //System.out.println("ERP_desc: " + record[1]);
                        prd.setItemErpDesc(record[1]);

                        //System.out.println("ERP_brand_size: " + record[2]);
                        prd.setItemErpBrandSize(record[2]);

                        //System.out.println("qty: " + record[3]);
                        prd.setItemErpUnit(record[4]);

                        //System.out.println("ERP_unit: " + record[4]);
                        prd.setQty(Float.parseFloat(record[3]));

                        //System.out.println("est_cost: " + record[5]);
                        prd.setEstCost(Float.parseFloat(record[5]));

                        //System.out.println("target_date: " + record[6]);
                        //prd.setTargetDate(new SimpleDateFormat("yyyy-MM-dd").parse(record[6]));

                        //System.out.println("-----------" + prd.toString());

                        prs.createPRDetail(prmId, prd);

                    }

                    // close readers
                    reader.close();
                    csvReader.close();
                } catch (IOException e) {
                    // IO error
                    System.out.println("The buffered reader is closed");
                } finally {
                    // releases any system resources associated
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (csvReader != null) {
                        try {
                            csvReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }});
    }

    public void ApachePOIExcelReadPRDFile(String filename, int prmId, String userSsn) throws FileNotFoundException, IOException, ParseException {

        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Path destinationFile;
        Path prdUploadPath = Paths.get(rootDir + prdUploadLocation + userSsn);

        destinationFile = prdUploadPath.resolve(
                Paths.get(filename))
                .normalize().toAbsolutePath();

        FileInputStream excelFile = new FileInputStream(new File(destinationFile.toString()));

        Workbook workbook = new XSSFWorkbook(excelFile);

        excelFile.close();
        File temp = new File(destinationFile.toString());
        if (temp.exists())
            temp.delete();

        Sheet datatypeSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = datatypeSheet.iterator();
        iterator.next(); //ignore the first row

        try{
            while (iterator.hasNext()) {

                Row currentRow = iterator.next();

                ProcurementRequisitionDetail prd = new ProcurementRequisitionDetail();
                prd.setPrMainId(prmId);

                //System.out.println("ERP_code: " + currentRow.getCell(0));
                prd.setItemErpCode(null == currentRow.getCell(0).getStringCellValue()  ? "" : currentRow.getCell(0).getStringCellValue().trim());

                //System.out.println("ERP_desc: " + currentRow.getCell(1));
                prd.setItemErpDesc(null == currentRow.getCell(1).getStringCellValue() ? "" : currentRow.getCell(1).getStringCellValue().trim());

                //System.out.println("ERP_brand_size: " + (null == currentRow.getCell(2)) );
                prd.setItemErpBrandSize(null == currentRow.getCell(2) ? "" : currentRow.getCell(2).getStringCellValue().trim());

                //System.out.println("qty: " + currentRow.getCell(3));
                prd.setQty(null == currentRow.getCell(3) ? 0:
                        currentRow.getCell(3).getCellType() == CellType.NUMERIC ? Float.parseFloat(String.valueOf(currentRow.getCell(3))) : 0);

                //System.out.println("ERP_unit: " + currentRow.getCell(4));
                prd.setItemErpUnit(null == currentRow.getCell(4) ? "" : currentRow.getCell(4).getStringCellValue().trim());


                //System.out.println("est_cost: " + currentRow.getCell(5));
                prd.setEstCost(null == currentRow.getCell(5) ? 0: currentRow.getCell(5).getCellType() == CellType.NUMERIC ? Float.parseFloat(String.valueOf(currentRow.getCell(5))) : 0);

                //System.out.println("target_date: " + currentRow.getCell(6).getDateCellValue());

                if (null != currentRow.getCell(6)){
                    //+ simpleDateFormat.format(currentRow.getCell(6).getDateCellValue()));
                    String targetDate = simpleDateFormat.format(currentRow.getCell(6).getDateCellValue());
                    Date targetDate1 = new SimpleDateFormat("yyyy-MM-dd").parse(targetDate);
                    prd.setTargetDate(targetDate1);
                }


                //System.out.println(prd.toString());
                prs.createPRDetail(prmId, prd);

            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }


    }

    @Override
    public void store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            Path rootPath = Paths.get(rootDir);
            Files.copy(file.getInputStream(), rootPath.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        Path rootPath = Paths.get(rootDir);
        try {
            return Files.walk(rootPath, 1)
                    .filter(path -> !path.equals(rootPath))
                    .map(path -> rootPath.relativize(path));
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        Path rootPath = Paths.get(rootDir);
        return rootPath.resolve(filename);
    }


    public Path load(String filename, Path dir) {
        return dir.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public Resource loadAsResource(String filename, Path dir) {
        try {
            Path file = load(filename, dir);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        Path rootPath = Paths.get(rootDir);
        FileSystemUtils.deleteRecursively(rootPath.toFile());
    }

    @Override
    public void init() {
        Path rootPath = Paths.get(rootDir);
        Path prPath = Paths.get(rootDir + prLocation);
        Path prdTemplatePath = Paths.get(rootDir + prdTemplateLocation);
        Path prdUploadPath = Paths.get(rootDir + prdUploadLocation);
        try {
            Files.createDirectory(rootPath);
            Files.createDirectories(prPath);
            Files.createDirectories(prdTemplatePath);
            Files.createDirectories(prdUploadPath);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }


}
