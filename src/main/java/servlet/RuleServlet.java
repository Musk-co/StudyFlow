package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.JsonObject;
import dao.AdaptRuleDAO;
import dao.impl.AdaptRuleDAOImpl;
import model.AdaptRule;

@WebServlet("/RuleServlet")
public class RuleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AdaptRuleDAO ruleDAO;

    @Override
    public void init() throws ServletException {
        ruleDAO = new AdaptRuleDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            List<AdaptRule> rules = ruleDAO.getAllRules();
            request.setAttribute("rules", rules);
            request.getRequestDispatcher("rules.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error loading rules");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JsonObject jsonResponse = new JsonObject();

        try {
            String action = request.getParameter("action");
            
            if (action == null) {
                // Handle new rule creation
                String ruleName = request.getParameter("ruleName");
                String conditionText = request.getParameter("conditionText");
                String actionText = request.getParameter("actionText");

                AdaptRule newRule = new AdaptRule();
                newRule.setRuleName(ruleName);
                newRule.setConditionText(conditionText);
                newRule.setActionText(actionText);
                newRule.setActive(true);

                ruleDAO.createRule(newRule);
                jsonResponse.addProperty("success", true);
                jsonResponse.addProperty("message", "Rule created successfully");
            } else {
                switch (action) {
                    case "toggle":
                        int ruleId = Integer.parseInt(request.getParameter("ruleId"));
                        AdaptRule rule = ruleDAO.getRuleById(ruleId);
                        if (rule != null) {
                            rule.setActive(!rule.isActive());
                            ruleDAO.updateRule(rule);
                            jsonResponse.addProperty("success", true);
                        } else {
                            jsonResponse.addProperty("success", false);
                            jsonResponse.addProperty("message", "Rule not found");
                        }
                        break;

                    case "delete":
                        ruleId = Integer.parseInt(request.getParameter("ruleId"));
                        ruleDAO.deleteRule(ruleId);
                        jsonResponse.addProperty("success", true);
                        jsonResponse.addProperty("message", "Rule deleted successfully");
                        break;

                    default:
                        jsonResponse.addProperty("success", false);
                        jsonResponse.addProperty("message", "Invalid action");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Error: " + e.getMessage());
        }

        out.print(jsonResponse.toString());
        out.flush();
    }
} 