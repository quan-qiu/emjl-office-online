package net.csdcodes.restController;

import net.csdcodes.model.Asset;
import net.csdcodes.model.HardwareType;
import net.csdcodes.model.User;
import net.csdcodes.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/asset")
@CrossOrigin(origins = "http://localhost:8080")
public class AssetApiController {

    @Autowired
    private AssetService assetService;


    @GetMapping("/getType")
    public List<HardwareType> getHardwareTypes() {
        List hardwareTypes = assetService.getType();
        return hardwareTypes;
    }

    @GetMapping("/getStatus")
    public List<HardwareType> getHardwareStatus() {
        List hardwareStatus = assetService.getStatus();
        return hardwareStatus;
    }

    @RequestMapping(value = "/getAsset/{name}/{classify}/{status}", method = RequestMethod.GET, produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Asset>> getAsset(@PathVariable(name = "name", required = false) String name,
                                                @PathVariable(name = "classify", required = false) String classify,
                                                @PathVariable(name = "status", required = false) String status) {
        try {
            //ResponseEntity<Asset> responseEntity = new ResponseEntity<Asset>("getAsset", HttpStatus.OK);
            ResponseEntity<List<Asset>> responseEntity =
                    new ResponseEntity<List<Asset>>(assetService.getAsset(name, classify,status), HttpStatus.OK);

            return responseEntity;
        } catch (Exception e) {
            return new ResponseEntity<List<Asset>>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getAsset/{ssn}", method = RequestMethod.GET, produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Asset>> getAsset(@PathVariable(name = "name", required = false) String ssn) {
        try {
            //ResponseEntity<Asset> responseEntity = new ResponseEntity<Asset>("getAsset", HttpStatus.OK);
            ResponseEntity<List<Asset>> responseEntity =
                    new ResponseEntity<List<Asset>>(assetService.getAsset(ssn), HttpStatus.OK);

            return responseEntity;
        } catch (Exception e) {
            return new ResponseEntity<List<Asset>>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/save", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<Asset> saveAsset(@RequestBody Asset asset) {
        try {
            int id = assetService.save(asset);
            Asset result = assetService.getAssetById(id);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity("Error on server", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<Asset> updateAsset(@RequestBody Asset asset) {
        //System.out.println(asset.toString());
        try{
            int id = assetService.update(asset);
            Asset result = assetService.getAssetById(id);
            return ResponseEntity.ok().body(result);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity("Error on server", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public String deleteAsset(@PathVariable("id") int id) {
        int result = assetService.deleteAsset(id);
        return Integer.toString(result);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getAssetById(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(assetService.getAssetById(id));
        } catch (SQLException se) {
            se.printStackTrace();
            return new ResponseEntity("Wrong id", HttpStatus.NOT_FOUND);
            //throw new ResponseStatusException(NOT_FOUND, String.format("No resource found for id (%s)", id));
        }
    }



}
