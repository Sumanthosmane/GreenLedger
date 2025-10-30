package com.greenledger.app.utils;

import com.greenledger.app.models.enums.UserRole;

/**
 * Permission Manager for role-based access control
 * Defines what actions each user role can perform
 */
public class PermissionManager {

    /**
     * Check if user can access financial data (expenses, revenue, profits)
     * @param role User's role
     * @return true if user has permission
     */
    public static boolean canAccessFinance(UserRole role) {
        return role == UserRole.FARMER || role == UserRole.BUSINESS_PARTNER;
    }

    /**
     * Check if user can manage labour (add, edit, delete labour records)
     * @param role User's role
     * @return true if user has permission
     */
    public static boolean canManageLabour(UserRole role) {
        return role == UserRole.FARMER;
    }

    /**
     * Check if user can view crop inventory
     * @param role User's role
     * @return true if user has permission
     */
    public static boolean canViewCropInventory(UserRole role) {
        return role == UserRole.FARMER || role == UserRole.BUSINESS_PARTNER;
    }

    /**
     * Check if user can edit crop data (add, update, delete crops)
     * @param role User's role
     * @return true if user has permission
     */
    public static boolean canEditCropData(UserRole role) {
        return role == UserRole.FARMER;
    }

    /**
     * Check if user can generate reports
     * @param role User's role
     * @return true if user has permission
     */
    public static boolean canGenerateReports(UserRole role) {
        return role == UserRole.FARMER || role == UserRole.BUSINESS_PARTNER;
    }

    /**
     * Check if user can view their own work records
     * @param role User's role
     * @return true if user has permission
     */
    public static boolean canViewOwnWork(UserRole role) {
        return role == UserRole.LABOURER;
    }

    /**
     * Check if user can manage storage facilities
     * @param role User's role
     * @return true if user has permission
     */
    public static boolean canManageStorage(UserRole role) {
        return role == UserRole.FARMER;
    }

    /**
     * Check if user can view storage inventory
     * @param role User's role
     * @return true if user has permission
     */
    public static boolean canViewStorage(UserRole role) {
        return role == UserRole.FARMER || role == UserRole.BUSINESS_PARTNER;
    }

    /**
     * Check if user can manage farm operations (add, edit, delete)
     * @param role User's role
     * @return true if user has permission
     */
    public static boolean canManageFarmOperations(UserRole role) {
        return role == UserRole.FARMER;
    }

    /**
     * Check if user can view farm operations
     * @param role User's role
     * @return true if user has permission
     */
    public static boolean canViewFarmOperations(UserRole role) {
        return true; // All roles can view operations
    }

    /**
     * Check if user can manage expenses (add, edit, delete)
     * @param role User's role
     * @return true if user has permission
     */
    public static boolean canManageExpenses(UserRole role) {
        return role == UserRole.FARMER;
    }

    /**
     * Check if user can view expenses
     * @param role User's role
     * @return true if user has permission
     */
    public static boolean canViewExpenses(UserRole role) {
        return role == UserRole.FARMER || role == UserRole.BUSINESS_PARTNER;
    }

    /**
     * Check if user can manage raw materials (add, edit, delete)
     * @param role User's role
     * @return true if user has permission
     */
    public static boolean canManageRawMaterials(UserRole role) {
        return role == UserRole.FARMER;
    }

    /**
     * Check if user can view raw materials
     * @param role User's role
     * @return true if user has permission
     */
    public static boolean canViewRawMaterials(UserRole role) {
        return role == UserRole.FARMER || role == UserRole.BUSINESS_PARTNER;
    }

    /**
     * Check if user can export data (PDF, Excel, CSV)
     * @param role User's role
     * @return true if user has permission
     */
    public static boolean canExportData(UserRole role) {
        return role == UserRole.FARMER || role == UserRole.BUSINESS_PARTNER;
    }

    /**
     * Check if user can modify user profiles (including bank details)
     * @param role User's role
     * @return true if user has permission
     */
    public static boolean canModifyProfile(UserRole role) {
        return true; // All users can modify their own profile
    }

    /**
     * Check if user can view analytics and dashboards
     * @param role User's role
     * @return true if user has permission
     */
    public static boolean canViewAnalytics(UserRole role) {
        return role == UserRole.FARMER || role == UserRole.BUSINESS_PARTNER;
    }

    /**
     * Check if user has administrative privileges
     * @param role User's role
     * @return true if user has admin access
     */
    public static boolean isAdmin(UserRole role) {
        return role == UserRole.FARMER;
    }

    /**
     * Get access level for the role (higher = more permissions)
     * @param role User's role
     * @return access level integer
     */
    public static int getAccessLevel(UserRole role) {
        return role.getAccessLevel();
    }

    /**
     * Check if role has sufficient access level
     * @param role User's role
     * @param requiredLevel Minimum required access level
     * @return true if user meets requirement
     */
    public static boolean hasAccessLevel(UserRole role, int requiredLevel) {
        return role.getAccessLevel() >= requiredLevel;
    }
}
