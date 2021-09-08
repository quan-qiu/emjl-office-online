package net.csdcodes.controller;

import net.csdcodes.model.Asset;
import net.csdcodes.model.Calendar;
import net.csdcodes.service.CalendarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
/**
 * creator: Quan Qiu
 * date: 06/03/21
 */
@Controller
@RequestMapping("/calendar")
public class CalendarController {

    Logger logger = LoggerFactory.getLogger(CalendarController.class);

    @Autowired
    private CalendarService calendarService;

    @GetMapping("/new")
    public String showNewCalendarPage(Model model){
        Calendar calendar = new Calendar();
        model.addAttribute("calendar", calendar);

        return "calendar_new";
    }

    @GetMapping("/edit")
    public String getEditor() {
        return "calendar_list";
    }

    @GetMapping("/{year}")
    public String listSpecialDate(@PathVariable Integer year, Model model){

        try {
            List<Calendar> specialDays = calendarService.getSpecialDay(year);
            if (specialDays.size()== 0){
                model.addAttribute("specialDays",null);
            }else{
                model.addAttribute("specialDays",specialDays);
            }

        }catch (Exception e){
            ResponseEntity<List<Asset>> responseEntity =new ResponseEntity<List<Asset>>(HttpStatus.BAD_REQUEST);
        }

        return "calendar_edit";
    }
}
