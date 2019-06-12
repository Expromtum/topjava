package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.MealRepositoryInMemory;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final String FORM_INSERT_OR_EDIT = "/meal.jsp";
    private static final String FORM_LIST = "/meals.jsp";
    private static final String MEALS = "meals";

    private static final Logger log = getLogger(MealServlet.class);
    private MealRepository repository = new MealRepositoryInMemory();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories")));

        log.info(meal.isNew() ? "Meals Create {}" : "Meals Update {}", id);
        repository.save(meal);

        //при redirect атрибуты теряются
        response.sendRedirect(MEALS);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if ("CREATE".equalsIgnoreCase(action)) {
            log.info("Meals Create start");

            // Set Current date and time without millisseconds
            final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),"", 100);

            request.setAttribute("meal", meal);
            request.getRequestDispatcher(FORM_INSERT_OR_EDIT).forward(request, response);
        } else if ("UPDATE".equalsIgnoreCase(action)) {
            int id = getId(request);
            log.info("Meals Update {}", id);

            Meal meal = repository.get(id);
            request.setAttribute("meal", meal);
            request.getRequestDispatcher(FORM_INSERT_OR_EDIT).forward(request, response);
        } else if ("DELETE".equalsIgnoreCase(action)) {
            int id = getId(request);
            log.info("Meals Delete {}", id);
            repository.delete(id);

            //при redirect атрибуты теряются
            response.sendRedirect(MEALS);
        } else if (action == null) {
            log.info("redirect to Meals");
            request.setAttribute("list", MealsUtil.getFilteredWithExcessInOnePass(repository.getAll(), 2000));
            request.getRequestDispatcher(FORM_LIST).forward(request, response);
        } else {
            throw new IllegalArgumentException("Action not defined!");
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }
}
