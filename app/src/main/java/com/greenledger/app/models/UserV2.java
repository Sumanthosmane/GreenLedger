package com.greenledger.app.models;

import com.greenledger.app.models.embedded.Address;
import com.greenledger.app.models.embedded.BankDetails;
import com.greenledger.app.models.enums.UserRole;

/**
 * Enhanced User model for v2.0
 * Contains complete user profile with role-based access
 */
public class UserV2 {
    private String userId;
    private PersonalInfo personalInfo;
    private BankDetails bankDetails;
    private Preferences preferences;
    private Metadata metadata;

    public UserV2() {
        // Required empty constructor for Firebase
    }

    public UserV2(String userId, String name, String phone, UserRole role) {
        this.userId = userId;
        this.personalInfo = new PersonalInfo();
        this.personalInfo.setName(name);
        this.personalInfo.setPhone(phone);
        this.personalInfo.setRole(role.name());
        this.preferences = new Preferences();
        this.metadata = new Metadata();
        this.metadata.setCreatedAt(System.currentTimeMillis());
        this.metadata.setLastLogin(System.currentTimeMillis());
        this.metadata.setActive(true);
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    public BankDetails getBankDetails() {
        return bankDetails;
    }

    public void setBankDetails(BankDetails bankDetails) {
        this.bankDetails = bankDetails;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    // Convenience methods
    public String getName() {
        return personalInfo != null ? personalInfo.getName() : "";
    }

    public String getPhone() {
        return personalInfo != null ? personalInfo.getPhone() : "";
    }

    public UserRole getRole() {
        if (personalInfo != null && personalInfo.getRole() != null) {
            try {
                return UserRole.valueOf(personalInfo.getRole());
            } catch (Exception e) {
                return UserRole.FARMER;
            }
        }
        return UserRole.FARMER;
    }

    // Nested classes
    public static class PersonalInfo {
        private String name;
        private String phone;
        private String email;
        private String role; // Store as string for Firebase
        private String profilePhoto;
        private Address address;

        public PersonalInfo() {
        }

        // Getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public UserRole getUserRole() {
            try {
                return UserRole.valueOf(role);
            } catch (Exception e) {
                return UserRole.FARMER;
            }
        }

        public void setUserRole(UserRole userRole) {
            this.role = userRole.name();
        }

        public String getProfilePhoto() {
            return profilePhoto;
        }

        public void setProfilePhoto(String profilePhoto) {
            this.profilePhoto = profilePhoto;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }
    }

    public static class Preferences {
        private String language;
        private String currency;
        private boolean notifications;

        public Preferences() {
            this.language = "English";
            this.currency = "INR";
            this.notifications = true;
        }

        // Getters and setters
        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public boolean isNotifications() {
            return notifications;
        }

        public void setNotifications(boolean notifications) {
            this.notifications = notifications;
        }
    }

    public static class Metadata {
        private long createdAt;
        private long lastLogin;
        private boolean isActive;

        public Metadata() {
        }

        // Getters and setters
        public long getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(long createdAt) {
            this.createdAt = createdAt;
        }

        public long getLastLogin() {
            return lastLogin;
        }

        public void setLastLogin(long lastLogin) {
            this.lastLogin = lastLogin;
        }

        public boolean isActive() {
            return isActive;
        }

        public void setActive(boolean active) {
            isActive = active;
        }
    }
}
