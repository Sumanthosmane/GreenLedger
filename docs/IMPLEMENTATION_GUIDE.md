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

## Phase 1: Foundation ✅

### Overview
The foundation phase has been successfully implemented with all core features and dependencies integrated.

### Completed Components

#### Authentication & User Management
- Phone-based Firebase authentication
- Enhanced user profiles (UserV2)
- Role-based access control infrastructure
- Secure data access rules

#### Data Layer
- Room Database for offline support
- Firebase Realtime Database integration
- File storage system for documents/images
- Sync management system

#### Base Features
- Expense tracking system
- Raw materials inventory
- Basic labour management
- Multi-language support foundation

### Implementation Details

#### Enum Classes (Implemented) ✅
Package: `com.greenledger.app.models.enums`
- UserRole.java (FARMER, LABOURER, BUSINESS_PARTNER)
- PaymentMode.java (CASH, ONLINE, UPI, CHEQUE, CREDIT)
- PaymentStatus.java (PAID, PENDING, PARTIAL)
- CropStatus.java (PLANNING, SOWING, GROWING, HARVESTING, COMPLETED)
- CropCategory.java (KHARIF, RABI, ZAID)
- ShiftType.java (FULL_DAY, HALF_DAY, HOURLY)
- StorageType.java (BARN, WAREHOUSE, COLD_STORAGE, OPEN_STORAGE)
- WorkType.java (PLOWING, SOWING, IRRIGATION, HARVESTING, MAINTENANCE, OTHER)

#### Supporting Models (Implemented) ✅
Package: `com.greenledger.app.models.embedded`
- Address.java - Complete address structure
- BankDetails.java - Secure banking information
- Location.java - Location tracking support
- PaymentDetails.java - Payment tracking system
- WorkDetails.java - Labour management support
- CropInfo.java - Crop information structure
- Quality.java - Quality metrics tracking

## Phase 2: Core Features ✅

### Overview
All core farm management features have been successfully implemented and tested.

### Completed Components

#### Farm Management System ✅
- Complete farm CRUD operations
- Land plot segmentation and tracking
- Location and soil management
- Irrigation system tracking
- Farm metadata management

#### Crop Lifecycle Management ✅
- Full crop lifecycle tracking
- Stage-wise documentation
- Photo documentation support
- Cost allocation per stage
- Quality metrics monitoring
- Harvest management
- Yield tracking

#### Storage Management ✅
- Multiple storage facility support
- Real-time inventory tracking
- Capacity management
- Storage cost allocation
- Quality preservation monitoring

### Implementation Details

#### Database Schema
The following schema has been implemented in Firebase:

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
  }
}
```

## Phase 3: Advanced Features ✅

### Overview
Role-based access control and advanced features have been successfully implemented.

### Completed Components

#### Access Control System ✅
- Multi-role support (Farmer, Labour, Business Partner)
- Role-specific interfaces
- Secure data access implementation
- Permission management system

#### Business Operations ✅
- Business partner management
- Enhanced financial tracking
- Multiple payment modes support
- Basic reporting system

#### Enhanced Features ✅
- Offline data synchronization
- Image upload and management
- Basic analytics dashboard
- Export functionality (PDF/Excel)
- Half-Day Work Support (Nov 15, 2025) ✅
  - Shift type selection (Full Day, Half Day, Hourly)
  - Auto-calculation of hours based on shift type
  - Display of shift type in labour list
  - Persistent storage in Firebase

### Enhanced Labour Management - Half-Day Work Implementation

#### Overview
The half-day work feature allows farmers to record labour work in three shift types: Full Day (8 hours), Half Day (4 hours), and Hourly (custom hours). This enhancement provides greater flexibility in labour scheduling and accurate wage calculation.

#### Implementation Details

**1. Model Updates**
- File: `com.greenledger.app.models.Labour.java`
- Added field: `private String shiftType;`
- Added constructor: `Labour(String labourId, String userId, String name, String phone, double hoursWorked, double hourlyRate, String workDate, String workDescription, ShiftType shiftType)`
- Added methods:
  - `getShiftType()` - Returns shift type as String
  - `setShiftType(String shiftType)` - Set shift type from String
  - `setShiftType(ShiftType shiftType)` - Set shift type from enum
  - `getShiftTypeEnum()` - Returns ShiftType enum (with null/invalid handling)

**2. Enum Support**
- File: `com.greenledger.app.models.enums.ShiftType.java`
- Already implemented with three values:
  - `FULL_DAY("Full Day")` - 8 hours default
  - `HALF_DAY("Half Day")` - 4 hours default
  - `HOURLY("Hourly")` - Custom hours
- Includes `getDisplayName()` method for UI display

**3. UI Components**
- File: `com.greenledger.app.res.layout.dialog_add_labour.xml`
- Added: `MaterialAutoCompleteTextView` for shift type selection
- Features:
  - Dropdown list with three shift options
  - Positioned between phone and hours fields
  - Styling consistent with Material Design 3

- File: `com.greenledger.app.res.layout.item_labour.xml`
- Added: `TextView` to display shift type
- Features:
  - Shows "Shift: [Shift Type]" format
  - Positioned below phone number
  - Color and size matching other secondary info

**4. Activity Implementation**
- File: `com.greenledger.app.activities.LabourActivity.java`
- Enhanced methods:
  - `showAddLabourDialog()` - Added shift type spinner setup
  - `setupShiftTypeSpinner()` - New method for shift type configuration
  - `saveLabourEntry()` - Updated to pass ShiftType parameter
- Auto-calculation logic:
  - Full Day → 8 hours
  - Half Day → 4 hours
  - Hourly → Empty (user enters hours)
- State management: `private ShiftType selectedShiftType = ShiftType.FULL_DAY;`

**5. Adapter Updates**
- File: `com.greenledger.app.adapters.LabourAdapter.java`
- Enhanced ViewHolder:
  - Added: `TextView shiftTypeText`
  - Updated `bind()` method to display shift type
  - Shows shift type using `labour.getShiftTypeEnum().getDisplayName()`

**6. String Resources**
- File: `com.greenledger.app.res.values.strings.xml`
- Added strings:
  - `<string name="shift_type">Shift Type</string>`
  - `<string name="full_day">Full Day</string>`
  - `<string name="half_day">Half Day</string>`
  - `<string name="hourly">Hourly</string>`

**7. Database Schema**
- Firebase field: `workDetails.shiftType`
- Type: String
- Format: Enum name (FULL_DAY, HALF_DAY, HOURLY)
- Default: FULL_DAY
- Backward compatible: Existing entries without shiftType default to FULL_DAY

**8. Payment Calculation**
- Formula: `Total Pay = Hours Worked × Hourly Rate`
- Shift type affects hours automatically:
  - Full Day: 8 hours (or user override)
  - Half Day: 4 hours (or user override)
  - Hourly: User-specified hours
- No changes to `getTotalPay()` method (already works correctly)

#### Testing
- Comprehensive test suite created: `HALF_DAY_WORK_TESTING.md`
- 10 test cases covering all scenarios:
  - T1: Add Full Day entry
  - T2: Add Half Day entry
  - T3: Add Hourly entry (custom hours)
  - T4: Shift type change (Full Day → Half Day)
  - T5: Shift type change (Half Day → Hourly)
  - T6: Display shift type in list
  - T7: Payment calculation verification
  - T8: Validation - missing shift type
  - T9: UI responsiveness
  - T10: Data persistence

#### Build Status
- Gradle Build: ✅ SUCCESSFUL (Nov 15, 2025)
- Compilation: ✅ Clean (minor deprecation warnings)
- Build Time: 1m 3s
- APK Assembly: ✅ Successful

### Security Implementation
The following Firebase security rules have been implemented:

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
    "labour": {
      "$labourId": {
        ".read": "auth != null",
        ".write": "auth != null && (!data.exists() || data.child('farmerId').val() === auth.uid || data.child('labourerId').val() === auth.uid)"
      }
    }
  }
}
```

---

## Phase 4: Analytics ✅

### Overview
All analytics and reporting features have been successfully implemented.

### Completed Components

#### Report Generation System ✅
- Profit & Loss reports
- Crop Yield reports
- Labour reports (including shift type data)
- PDF export functionality
- Excel export functionality

#### Data Visualization ✅
- Interactive charts using MPAndroidChart
- Multiple chart types (Line, Bar, Pie)
- Custom styling and animations
- Real-time data updates

#### Analytics Dashboard ✅
- Key Performance Indicators (KPIs)
- Revenue trends
- Expense distribution
- Labour analysis (with shift type breakdown)

### Implementation Details

#### Report Models (Implemented) ✅
All report models have been implemented in the package `com.greenledger.app.models.reports`:

1. **ProfitLossReport.java**
   - Transaction tracking
   - Revenue categorization
   - Expense breakdown
   - Net profit calculation

2. **CropYieldReport.java**
   - Expected vs actual yield
   - Quality metrics
   - Cost breakdown
   - Profitability analysis

3. **LabourReport.java**
   - Work entry tracking (including shift types)
   - Hours and wages calculation
   - Worker performance metrics
   - Payment status tracking
   - Shift type distribution analysis

#### Report Generation (Implemented) ✅
The `ReportGenerator` class in `com.greenledger.app.utils` provides:

1. **PDF Generation**
   - Using iText7 library
   - Professional formatting
   - Tabular data support
   - Charts and graphs
   - Digital signatures

2. **Excel Export**
   - Using Apache POI
   - Multiple worksheets
   - Data formatting
   - Formula support
   - Auto-sized columns

#### Data Visualization (Implemented) ✅
The `ChartHelper` class in `com.greenledger.app.utils` provides:

1. **Chart Types**
   - Line charts for trends
   - Bar charts for comparisons
   - Pie charts for distributions
   - Custom styling and animations

2. **Features**
   - Interactive zooming
   - Touch gestures
   - Value highlighting
   - Legend customization
   - Real-time updates

#### Analytics Dashboard (Implemented) ✅
The `AnalyticsDashboardActivity` provides:

1. **KPI Cards**
   - Total revenue
   - Total expenses
   - Net profit
   - Real-time updates

2. **Charts**
   - Expense distribution (Pie)
   - Revenue trends (Line)
   - Labour analysis (Bar) - including shift type breakdown
   - Interactive controls

3. **Actions**
   - Generate reports
   - Export data
   - Date range selection
   - Filter options

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
- Charts (expense breakdown, crop yield trends, labour shift distribution)
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

## Document Control
- **Version**: 2.1
- **Date**: November 9, 2025
- **Status**: Phase 4 Completed
- **Next Phase**: Testing & Deployment
- **Next Review**: November 23, 2025

---

**Last Updated**: October 31, 2025
**Version**: 2.0
**Status**: Implementation Guide
