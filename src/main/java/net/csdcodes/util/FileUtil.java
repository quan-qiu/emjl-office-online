package net.csdcodes.util;

import net.csdcodes.service.exception.StorageException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class FileUtil {
    public static int noOfQuickServiceThreads = 20;


    /**
     * this statement create a thread pool of twenty threads
     * here we are assigning send mail task using ScheduledExecutorService.submit();
     */
    private ScheduledExecutorService quickService = Executors.newScheduledThreadPool(noOfQuickServiceThreads); // Creates a thread pool that reuses fixed number of threads(as specified by noOfThreads in this case).


    public void copyFileToDestination(MultipartFile file, Path destinationFile) {
        quickService.submit(new Runnable() {
            @Override
            public void run() {
                try (InputStream inputStream = file.getInputStream()) {
                    File temp = new File(destinationFile.toString());

                    if (temp.exists()) {
                        temp.delete();
                    }

                    Files.copy(inputStream, destinationFile,
                            StandardCopyOption.REPLACE_EXISTING);
                    System.out.println(destinationFile.toString());

                } catch (IOException ioe) {
                    throw new StorageException("Failed to store file " + file.getOriginalFilename(), ioe);
                    //e.printStackTrace();

                }

            }
        });
    }
}