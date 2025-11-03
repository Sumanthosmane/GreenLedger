# GreenLedger v2.0 - Implementation Guide

## Overview

This guide provides step-by-step instructions for implementing the comprehensive farm management features in GreenLedger v2.0. Follow this guide systematically to ensure proper implementation, testing, and deployment.

---

## Table of Contents

1. [Prerequisites](#prerequisites)
2. [Phase 1: Foundation](#phase-1-foundation)
3. [Phase 2: Core Features](#phase-2-core-features)
4. [Phase 3: Advanced Features](#phase-3-advanced-features)
5. [Phase 4: Analytics](#phase-4-analytics)
6. [Phase 5: Testing & Deployment](#phase-5-testing--deployment)
7. [Build & Test Checklist](#build--test-checklist)

---

## Prerequisites

### Development Environment
- ✅ Android Studio Hedgehog 2023.1.1+
- ✅ JDK 17 or 21
- ✅ Android SDK API 34
- ✅ Firebase project configured
- ✅ Git for version control

### Dependencies to Add

Update `app/build.gradle`:

```gradle
dependencies {
    // Existing dependencies...

    // Room Database for offline support
    implementation "androidx.room:room-runtime:2.6.0"
    annotationProcessor "androidx.room:room-compiler:2.6.0"

    // PDF Generation
    implementation 'com.itextpdf:itext7-core:7.2.5'

    // Charts
    implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'

    // Excel Export
    implementation 'org.apache.poi:poi:5.2.5'
    implementation 'org.apache.poi:poi-ooxml:5.2.5'

    // Image Loading
    implementation 'com.github.bumptech.glide:glide:4.16.0'
    annotationProcessor 'com.github.bumptech.glide:compiler:4.16.0'

    // Gson for JSON parsing
    implementation 'com.google.code.gson:gson:2.10.1'
}
```

Add to `settings.gradle`:
```gradle
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' } // For MPAndroidChart
    }
}
```

---

## Phase 1: Foundation

### Step 1.1: Create Enum Classes

Create package: `com.greenledger.app.models.enums`

**Files to create**:
1. `UserRole.java` - FARMER, LABOURER, BUSINESS_PARTNER
2. `PaymentMode.java` - CASH, ONLINE, UPI, CHEQUE, CREDIT
3. `PaymentStatus.java` - PAID, PENDING, PARTIAL
4. `CropStatus.java` - PLANNING, SOWING, GROWING, HARVESTING, COMPLETED
5. `CropCategory.java` - KHARIF, RABI, ZAID
6. `ShiftType.java` - FULL_DAY, HALF_DAY, HOURLY
7. `StorageType.java` - BARN, WAREHOUSE, COLD_STORAGE, OPEN_STORAGE
8. `WorkType.java` - PLOWING, SOWING, IRRIGATION, HARVESTING, MAINTENANCE, OTHER

### Step 1.2: Create Supporting Model Classes

Create package: `com.greenledger.app.models.embedded`

**Files to create**:
1. `Address.java` - street, village, district, state, pincode
2. `BankDetails.java` - accountHolderName, accountNumber, ifscCode, bankName, upiId
3. `Location.java` - latitude, longitude, address
4. `PaymentDetails.java` - mode, status, paidAmount, pendingAmount, transactionId
5. `WorkDetails.java` - date, checkIn, checkOut, workType, hoursWorked
6. `CropInfo.java` - type, variety, category
7. `Quality.java` - grade, moistureLevel, purityLevel
8. `Inventory.java` - total, sold, unsold, spoiled, reserved

### Step 1.3: Enhance User Model

Update `User.java`:

```java
package com.greenledger.app.models;

import com.greenledger.app.models.embedded.Address;
import com.greenledger.app.models.embedded.BankDetails;
import com.greenledger.app.models.enums.UserRole;

public class User {
    private String userId;
    private PersonalInfo personalInfo;
    private BankDetails bankDetails;
    private Preferences preferences;
    private Metadata metadata;

    public static class PersonalInfo {
        private String name;
        private String phone;
        private String email;
        private UserRole role;
        private String profilePhoto;
        private Address address;

        // Getters and setters
    }

    public static class Preferences {
        private String language;
        private String currency;
        private boolean notifications;

        // Getters and setters
    }

    public static class Metadata {
        private long createdAt;
        private long lastLogin;
        private boolean isActive;

        // Getters and setters
    }

    // Constructors, getters and setters
}
```

### Step 1.4: Create Permission Manager

Create `PermissionManager.java`:

```java
package com.greenledger.app.utils;

import com.greenledger.app.models.enums.UserRole;

public class PermissionManager {

    public static boolean canAccessFinance(UserRole role) {
        return role == UserRole.FARMER;
    }

    public static boolean canManageLabour(UserRole role) {
        return role == UserRole.FARMER;
    }

    public static boolean canViewCropInventory(UserRole role) {
        return role != UserRole.LABOURER;
    }

    public static boolean canEditCropData(UserRole role) {
        return role == UserRole.FARMER;
    }

    public static boolean canGenerateReports(UserRole role) {
        return role == UserRole.FARMER;
    }

    public static boolean canAccessStorage(UserRole role) {
        return role == UserRole.FARMER;
    }

    public static boolean canPlaceOrders(UserRole role) {
        return role == UserRole.BUSINESS_PARTNER;
    }
}
```

### Step 1.5: Update Firebase Helper

Enhance `FirebaseHelper.java` with new references:

```java
public DatabaseReference getFarmsRef() {
    return database.child("farms");
}

public DatabaseReference getCropsRef() {
    return database.child("crops");
}

public DatabaseReference getStorageRef() {
    return database.child("storage");
}

public DatabaseReference getSalesRef() {
    return database.child("sales");
}

public DatabaseReference getSchedulesRef() {
    return database.child("schedules");
}

public DatabaseReference getPettyCashRef() {
    return database.child("pettyCash");
}

public DatabaseReference getBusinessPartnersRef() {
    return database.child("businessPartners");
}

// Query helpers
public DatabaseReference getUserFarms(String userId) {
    return database.child("farms").orderByChild("farmerId").equalTo(userId);
}

public DatabaseReference getFarmCrops(String farmId) {
    return database.child("crops").orderByChild("farmId").equalTo(farmId);
}
```

---

## Phase 2: Core Features ✅

### Farm Management Module ✅

#### Models
- Farm with FarmDetails and Metadata
- Land plots with area and status tracking
- Location with address, district, state, and PIN code

#### Features Implemented
1. Farm CRUD operations
2. Simple location input system
   - Manual address entry
   - District and state tracking
   - PIN code validation
3. Land plot tracking
4. Soil and irrigation type management

#### Implementation Notes
- Removed Google Maps dependency for cost efficiency
- Using simplified location input dialog
- Implemented Firebase Realtime Database rules
- Added image upload support using Firebase Storage

### Crop Management Module ✅

#### Models
- Crop with CropInfo and Lifecycle
- CropStage for tracking progress
- Quality and Quantity tracking

#### Features Implemented
1. Complete crop lifecycle tracking
2. Stage-wise documentation
3. Image upload and storage
4. Cost allocation per stage
5. Quality metrics tracking

#### Implementation Notes
- Using Firebase Storage for image management
- Implemented stage-based progress tracking
- Added validation for all input fields
- Integrated with farm and storage modules

### Storage Management Module ✅

#### Models
- Storage with StorageInfo and Metadata
- Inventory tracking
- StorageType enumeration

#### Features Implemented
1. Storage unit CRUD operations
2. Capacity and occupancy tracking
3. Basic inventory management
4. Cost allocation system

#### Implementation Notes
- Added validation for capacity limits
- Implemented real-time occupancy updates
- Created Firebase security rules for data access

### Database Schema Updates
```json
{
  "farms": {
    "$farmId": {
      "farmDetails": {
        "name": "string",
        "totalArea": "number",
        "soilType": "string",
        "irrigationType": "string",
        "location": {
          "address": "string",
          "district": "string",
          "state": "string",
          "pincode": "string"
        }
      },
      "lands": [
        {
          "landId": "string",
          "name": "string",
          "area": "number",
          "status": "string"
        }
      ],
      "metadata": {
        "createdAt": "timestamp",
        "updatedAt": "timestamp"
      }
    }
  },
  "crops": {
    "$cropId": {
      "cropInfo": {
        "type": "string",
        "variety": "string",
        "category": "string"
      },
      "lifecycle": {
        "status": "string",
        "stages": [
          {
            "name": "string",
            "timestamp": "number",
            "status": "string",
            "imageUrl": "string"
          }
        ],
        "sowingDate": "timestamp",
        "expectedHarvestDate": "timestamp"
      },
      "quantity": {
        "expected": "number",
        "actual": "number",
        "unit": "string"
      }
    }
  },
  "storage": {
    "$storageId": {
      "storageInfo": {
        "name": "string",
        "type": "string",
        "capacity": "number",
        "currentOccupancy": "number",
        "location": {
          "address": "string",
          "district": "string",
          "state": "string",
          "pincode": "string"
        }
      },
      "inventory": {
        "$itemId": {
          "cropId": "string",
          "quantity": "number"
        }
      }
    }
  }
}
```

### Firebase Security Rules
```json
{
  "rules": {
    "farms": {
      "$farmId": {
        ".read": "auth != null",
        ".write": "auth != null && (!data.exists() || data.child('farmerId').val() === auth.uid)"
      }
    },
    "crops": {
      "$cropId": {
        ".read": "auth != null",
        ".write": "auth != null && (!data.exists() || data.child('farmerId').val() === auth.uid)"
      }
    },
    "storage": {
      "$storageId": {
        ".read": "auth != null",
        ".write": "auth != null && (!data.exists() || data.child('farmerId').val() === auth.uid)"
      }
    }
  }
}
```

### Migration Notes
1. No database migrations needed for fresh installations
2. For existing users:
   - Run data migration scripts
   - Update security rules
   - Verify data integrity

### Testing Checklist ✅
- [x] Farm CRUD operations
- [x] Location input validation
- [x] Crop lifecycle tracking
- [x] Storage capacity management
- [x] Image upload and retrieval
- [x] Firebase security rules
- [x] Offline data persistence
- [x] Error handling
- [x] Input validation
- [x] UI responsiveness

---

## Phase 3: Advanced Features

### Step 3.1: Implement Business Partner Module

**Create Models**:
1. `BusinessPartner.java` - Partner details

**Create Activities**:
1. `BusinessPartnerActivity.java` - List partners
2. `AddPartnerActivity.java` - Add new partner
3. `PartnerTransactionsActivity.java` - Transaction history

**Key Features**:
- Partner registration
- Order placement
- Transaction tracking
- Communication channel

### Step 3.2: Implement Offline Support

**Create Room Database**:

```java
@Database(entities = {
    User.class,
    Farm.class,
    Crop.class,
    Expense.class,
    Labour.class,
    Sale.class
}, version = 1)
public abstract class GreenLedgerDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract FarmDao farmDao();
    public abstract CropDao cropDao();
    // ... other DAOs
}
```

**Create Sync Manager**:
```java
public class SyncManager {
    public void syncData() {
        // Sync local Room DB with Firebase
    }

    public void handleConflicts() {
        // Resolve sync conflicts
    }
}
```

---

## Phase 4: Analytics

### Step 4.1: Implement Report Generation

**Create Report Models**:
1. `ProfitLossReport.java`
2. `ExpenseReport.java`
3. `LabourReport.java`
4. `CropYieldReport.java`

**Create Report Generators**:

```java
public class ReportGenerator {
    public ProfitLossReport generatePL(Date from, Date to) {
        // Calculate P&L
    }

    public byte[] generatePDF(Report report) {
        // Generate PDF using iText
    }

    public byte[] generateExcel(Report report) {
        // Generate Excel using Apache POI
    }
}
```

**Create Activities**:
1. `ReportsActivity.java` - Report dashboard
2. `ProfitLossActivity.java` - P&L report
3. `ExpenseAnalysisActivity.java` - Expense charts

### Step 4.2: Implement Data Visualization

**Create Chart Helpers**:

```java
public class ChartHelper {
    public LineChart createLineChart(List<Entry> data) {
        // Create line chart using MPAndroidChart
    }

    public PieChart createPieChart(List<PieEntry> data) {
        // Create pie chart
    }

    public BarChart createBarChart(List<BarEntry> data) {
        // Create bar chart
    }
}
```

**Create Dashboard**:
- KPI cards (total revenue, expenses, profit)
- Charts (expense breakdown, crop yield trends)
- Quick actions
- Recent activity feed

---

## Phase 5: Testing & Deployment

### Step 5.1: Unit Testing

Create test classes for each component:

```java
@RunWith(AndroidJUnit4.class)
public class CropManagerTest {
    @Test
    public void testCropCreation() {
        // Test crop CRUD operations
    }

    @Test
    public void testCropLifecycle() {
        // Test stage transitions
    }
}
```

### Step 5.2: Integration Testing

Test Firebase integration:
- Authentication flow
- Data sync
- Offline support
- Real-time updates

### Step 5.3: UI/UX Testing

- Test on different screen sizes
- Test with different user roles
- Accessibility testing
- Performance testing

---

## Build & Test Checklist

### After Each Feature Implementation

- [ ] **Build**: Clean and rebuild project
  ```bash
  gradlew clean assembleDebug
  ```

- [ ] **Unit Tests**: Run unit tests
  ```bash
  gradlew test
  ```

- [ ] **Install**: Install on device/emulator
  ```bash
  gradlew installDebug
  ```

- [ ] **Manual Testing**:
  - Test happy path
  - Test error scenarios
  - Test edge cases
  - Test role-based access

- [ ] **Code Review**:
  - Check for memory leaks
  - Verify Firebase listeners are removed
  - Check null safety
  - Review security rules

- [ ] **Documentation**:
  - Update DEVELOPER_NOTES.md
  - Update API documentation
  - Add code comments
  - Update README if needed

- [ ] **Git Commit**:
  ```bash
  git add .
  git commit -m "Implement [feature name]"
  git push origin main
  ```

### Complete Application Testing

- [ ] **Functional Testing**:
  - All CRUD operations work
  - Role-based access enforced
  - Data validation works
  - Error handling appropriate

- [ ] **Integration Testing**:
  - Firebase sync works
  - Offline mode works
  - Reports generate correctly
  - Exports work (PDF/Excel)

- [ ] **Performance Testing**:
  - App starts < 3 seconds
  - Smooth scrolling
  - No ANR errors
  - Memory usage acceptable

- [ ] **Security Testing**:
  - Firebase rules enforced
  - Bank details encrypted
  - SQL injection prevented
  - XSS protection

- [ ] **Compatibility Testing**:
  - Test on API 24 (min SDK)
  - Test on API 34 (target SDK)
  - Test on different screen sizes
  - Test on different manufacturers

---

## Development Best Practices

### 1. Code Organization
- Follow package-by-feature structure
- Keep activities < 500 lines
- Extract business logic to managers
- Use meaningful variable names

### 2. Error Handling
```java
try {
    // Firebase operation
} catch (Exception e) {
    Log.e(TAG, "Error: " + e.getMessage(), e);
    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
}
```

### 3. Memory Management
```java
@Override
protected void onDestroy() {
    super.onDestroy();
    // Remove Firebase listeners
    if (valueEventListener != null) {
        ref.removeEventListener(valueEventListener);
    }
}
```

### 4. Null Safety
```java
if (user != null && user.getPersonalInfo() != null) {
    String name = user.getPersonalInfo().getName();
}
```

### 5. Loading States
```java
private void showLoading(boolean show) {
    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    contentView.setVisibility(show ? View.GONE : View.VISIBLE);
}
```

---

## Security Checklist

- [ ] All sensitive data encrypted
- [ ] Firebase security rules updated
- [ ] Input validation on all forms
- [ ] SQL injection prevention
- [ ] XSS protection
- [ ] Secure network communication (HTTPS)
- [ ] No hardcoded secrets
- [ ] ProGuard enabled for release
- [ ] Certificate pinning (if needed)
- [ ] Regular security audits

---

## Deployment Checklist

- [ ] Update version code and name
- [ ] Generate signed APK
- [ ] Test release build thoroughly
- [ ] Update Firebase production rules
- [ ] Backup production database
- [ ] Update Play Store listing
- [ ] Prepare release notes
- [ ] Monitor crash reports
- [ ] Monitor user feedback
- [ ] Plan rollout strategy

---

## Maintenance

### Regular Tasks

**Daily**:
- Monitor Firebase console for errors
- Check crash reports
- Review user feedback

**Weekly**:
- Database backup verification
- Performance metrics review
- Security scan

**Monthly**:
- Dependency updates
- Security audit
- Feature usage analysis
- User satisfaction survey

---

## Troubleshooting

### Common Issues

**Firebase Connection Issues**:
- Check google-services.json
- Verify Firebase services enabled
- Check network connectivity

**Build Errors**:
- Clean and rebuild
- Invalidate caches
- Check dependency versions

**Performance Issues**:
- Profile with Android Profiler
- Check for memory leaks
- Optimize database queries

**Sync Issues**:
- Check Firebase rules
- Verify network status
- Clear app data and retry

---

## Resources

- [Firebase Documentation](https://firebase.google.com/docs)
- [Android Developers](https://developer.android.com/)
- [Material Design Guidelines](https://m3.material.io/)
- [MPAndroidChart Wiki](https://github.com/PhilJay/MPAndroidChart/wiki)
- [Room Database Guide](https://developer.android.com/training/data-storage/room)

---

**Last Updated**: October 31, 2025
**Version**: 2.0
**Status**: Implementation Guide
