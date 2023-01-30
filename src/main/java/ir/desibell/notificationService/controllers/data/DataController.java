package ir.desibell.notificationService.controllers.data;

import com.google.gson.Gson;
import ir.desibell.notificationService.entities.data.Data;
import ir.desibell.notificationService.entities.subPanel.SubPanel;
import ir.desibell.notificationService.services.data.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/data")
public class DataController {
    
    @Autowired
    private DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }
    
    @GetMapping("/show")
    @ResponseBody
    public String showData(){
        return new Gson().toJson(this.dataService.findAllData());
    }
    
    @GetMapping("/register")
    public String registerDataPage(Model model){
        model.addAttribute("data", new Data(new SubPanel()));
        return "redirect:/data/show";
    }
    
    @PostMapping("/register")
    public String registerData(@ModelAttribute Data data){
        this.dataService.registerData(data);
        return "redirect:/data/show";
    }
    
}
