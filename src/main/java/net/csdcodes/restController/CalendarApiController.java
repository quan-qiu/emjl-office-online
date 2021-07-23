package net.csdcodes.restController;

import net.csdcodes.model.Asset;
import net.csdcodes.model.Calendar;
import net.csdcodes.model.HardwareType;
import net.csdcodes.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/calendar")
@CrossOrigin(origins = "http://localhost:8080")
public class CalendarApiController {

    @Autowired
    private CalendarService calendarService;

    @GetMapping("/years")
    public List<Integer> getYears() {
        LocalDate currentDay = LocalDate.now();

        int currentYear = currentDay.getYear();

        List<Integer> years = new ArrayList<>();

        for (int i= currentYear-2; i<=(currentYear+2); i++){
            years.add(i);
        }

        return years;
    }

    @GetMapping("/{year}")
    public ResponseEntity<List<Calendar>> getSpecialDaysOfYear(@PathVariable Integer year){

        try {

            ResponseEntity<List<Calendar>> responseEntity =
                    new ResponseEntity<List<Calendar>>(calendarService.getSpecialDay(year), HttpStatus.OK);

            return responseEntity;
        } catch (Exception e) {
            return new ResponseEntity<List<Calendar>>(HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(value = "/save", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE})
    public ResponseEntity<Calendar> saveAsset(@RequestBody Calendar calendar) {
        try {
            int id = calendarService.save(calendar);
            Calendar result = calendarService.getCalendarById(id);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return new ResponseEntity("Error on server", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping(value = "/delete/{id}")
    public String deleteSpecialDay(@PathVariable("id") int id) {
        int result = calendarService.deleteSpecialDay(id);
        return Integer.toString(result);
    }

    @GetMapping("/list/{year}")
    public ResponseEntity<List> getSpecialDays(@PathVariable Integer year){

        try {
            List<Calendar> specialDays=calendarService.getSpecialDay(year);
            ResponseEntity<List> responseEntity =
                    new ResponseEntity<List>(convertCalendarToList(specialDays), HttpStatus.OK);

            return responseEntity;
        } catch (Exception e) {
            return new ResponseEntity<List>(HttpStatus.BAD_REQUEST);
        }

    }

    private ArrayList<?>  convertCalendarToList(List<Calendar> days){
        ArrayList dayList = new ArrayList();

        for (Calendar day: days
             ) {

            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(day.getSpecialDate());
            int[] oneDay = new int[3];
            oneDay[0] = cal.get(java.util.Calendar.YEAR);
            oneDay[1] = cal.get(java.util.Calendar.MONTH);
            oneDay[2] = cal.get(java.util.Calendar.DAY_OF_MONTH);
            dayList.add(oneDay);

        }

        return dayList;
    }
}
