package dao.impl;

import dao.AdaptRuleDAO;
import model.AdaptRule;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdaptRuleDAOImpl implements AdaptRuleDAO {
    
    @Override
    public int create(AdaptRule rule) throws Exception {
        String sql = "INSERT INTO adapt_rules (rule_name, condition_text, action_text, is_active) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, rule.getRuleName());
            pstmt.setString(2, rule.getConditionText());
            pstmt.setString(3, rule.getActionText());
            pstmt.setBoolean(4, rule.isActive());
            
            pstmt.executeUpdate();
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return -1;
        }
    }
    
    @Override
    public AdaptRule getById(int ruleId) throws Exception {
        String sql = "SELECT * FROM adapt_rules WHERE rule_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, ruleId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAdaptRule(rs);
                }
            }
            return null;
        }
    }
    
    @Override
    public boolean update(AdaptRule rule) throws Exception {
        String sql = "UPDATE adapt_rules SET rule_name = ?, condition_text = ?, action_text = ?, is_active = ? " +
                    "WHERE rule_id = ?";
                    
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, rule.getRuleName());
            pstmt.setString(2, rule.getConditionText());
            pstmt.setString(3, rule.getActionText());
            pstmt.setBoolean(4, rule.isActive());
            pstmt.setInt(5, rule.getRuleId());
            
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public boolean delete(int ruleId) throws Exception {
        String sql = "DELETE FROM adapt_rules WHERE rule_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, ruleId);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public List<AdaptRule> getAll() throws Exception {
        String sql = "SELECT * FROM adapt_rules ORDER BY rule_name";
        List<AdaptRule> rules = new ArrayList<>();
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                rules.add(mapResultSetToAdaptRule(rs));
            }
        }
        return rules;
    }
    
    @Override
    public List<AdaptRule> getActiveRules() throws Exception {
        String sql = "SELECT * FROM adapt_rules WHERE is_active = true ORDER BY rule_name";
        List<AdaptRule> rules = new ArrayList<>();
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                rules.add(mapResultSetToAdaptRule(rs));
            }
        }
        return rules;
    }
    
    @Override
    public boolean toggleActive(int ruleId) throws Exception {
        String sql = "UPDATE adapt_rules SET is_active = NOT is_active WHERE rule_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, ruleId);
            return pstmt.executeUpdate() > 0;
        }
    }
    
    @Override
    public List<AdaptRule> getByName(String namePattern) throws Exception {
        String sql = "SELECT * FROM adapt_rules WHERE rule_name LIKE ? ORDER BY rule_name";
        List<AdaptRule> rules = new ArrayList<>();
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + namePattern + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    rules.add(mapResultSetToAdaptRule(rs));
                }
            }
        }
        return rules;
    }
    
    private AdaptRule mapResultSetToAdaptRule(ResultSet rs) throws SQLException {
        AdaptRule rule = new AdaptRule();
        rule.setRuleId(rs.getInt("rule_id"));
        rule.setRuleName(rs.getString("rule_name"));
        rule.setConditionText(rs.getString("condition_text"));
        rule.setActionText(rs.getString("action_text"));
        rule.setActive(rs.getBoolean("is_active"));
        rule.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        rule.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return rule;
    }
} 