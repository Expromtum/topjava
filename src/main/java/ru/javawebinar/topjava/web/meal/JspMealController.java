package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping(value = "/meals")
public class JspMealController extends AbstractMealController {

    @Autowired
    public JspMealController(MealService service) {
        super(service);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String doSave(HttpServletRequest request) {
        Meal meal = new Meal(
            LocalDateTime.parse(request.getParameter("dateTime")),
            request.getParameter("description"),
            Integer.parseInt(request.getParameter("calories")));

        if (StringUtils.isEmpty(request.getParameter("id"))) {
            this.create(meal);
        } else {
            this.update(meal, getId(request));
        }

        return "redirect:/meals";
    }

    @RequestMapping(value ="/delete", method = RequestMethod.GET)
    public String delete(HttpServletRequest request)  {
        int id = getId(request);
        this.delete(id);

        return "redirect:/meals";
    }

    @RequestMapping(value ="/update", method = RequestMethod.GET)
    public String update(HttpServletRequest request, Model model) {
        final Meal meal = this.get(getId(request));
        model.addAttribute("meal", meal);
        model.addAttribute("action", "update");
        return "mealForm";
    }

    @RequestMapping(value ="/create", method = RequestMethod.GET)
    public String create(Model model) {
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        model.addAttribute("action", "create");
        return "mealForm";
    }

    @RequestMapping(method = RequestMethod.GET)
    public String meals(HttpServletRequest request, Model model) {
        String action = request.getParameter("action");

        if ("filter".equals(action)) {
            LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
            LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
            LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
            LocalTime endTime = parseLocalTime(request.getParameter("endTime"));

            model.addAttribute("meals", this.getBetween(startDate, startTime, endDate, endTime));
        } else {
            model.addAttribute("meals", this.getAll());
        }
        return "meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
