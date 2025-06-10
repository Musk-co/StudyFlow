package dao;

import model.AdaptRule;
import java.util.List;

public interface AdaptRuleDAO {
    // Create a new adaptation rule
    int create(AdaptRule rule) throws Exception;
    
    // Retrieve a rule by ID
    AdaptRule getById(int ruleId) throws Exception;
    
    // Update an existing rule
    boolean update(AdaptRule rule) throws Exception;
    
    // Delete a rule
    boolean delete(int ruleId) throws Exception;
    
    // Get all rules
    List<AdaptRule> getAll() throws Exception;
    
    // Get all active rules
    List<AdaptRule> getActiveRules() throws Exception;
    
    // Toggle rule active status
    boolean toggleActive(int ruleId) throws Exception;
    
    // Get rules by name (partial match)
    List<AdaptRule> getByName(String namePattern) throws Exception;
} 