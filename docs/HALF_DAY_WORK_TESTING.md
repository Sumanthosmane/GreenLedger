# Half-Day Work Implementation - Testing Guide

**Date**: November 15, 2025  
**Feature**: Half-Day Work Support for Labour Management  
**Version**: 1.0

---

## Overview

This document outlines the testing procedures for the half-day work feature implemented in GreenLedger v2.0. The feature allows farmers to record labour work in three shift types: Full Day (8 hours), Half Day (4 hours), and Hourly (custom hours).

---

## Feature Description

### What Changed
- Added shift type selection to labour entry dialog
- Three shift types available: Full Day, Half Day, Hourly
- Auto-calculation of hours based on selected shift type
- Display of shift type in labour list items
- Database support for storing shift type information

### Implementation Details
- **Labour Model**: Added `shiftType` field with getter/setter methods
- **UI Components**: Added MaterialAutoCompleteTextView for shift type selection
- **Adapter**: Updated to display shift type in labour cards
- **Database**: Shift type stored as String for Firebase compatibility

---

## Test Cases

### 1. Add Full Day Labour Entry

**Test ID**: T1_FULL_DAY

**Preconditions**:
- User is logged in as a Farmer
- Navigation to Labour Management screen is complete
- Add Labour dialog is open

**Steps**:
1. Enter labour name: "Raj Kumar"
2. Enter phone number: "9876543210"
3. Select Shift Type: "Full Day"
4. Verify hours are auto-filled to 8.0
5. Enter hourly rate: "500"
6. Select work date: (current date)
7. Enter work description: "Plowing field"
8. Click Save

**Expected Result**:
- Labour entry is saved successfully
- Toast message shows "Labour entry added successfully"
- Entry appears in labour list with:
  - Name: "Raj Kumar"
  - Phone: "9876543210"
  - Shift: "Full Day"
  - Hours: 8.0
  - Rate: ₹500/hr
  - Total Pay: ₹4000.00

**Actual Result**: ___________________

**Status**: ☐ PASS ☐ FAIL

---

### 2. Add Half Day Labour Entry

**Test ID**: T2_HALF_DAY

**Preconditions**:
- User is logged in as a Farmer
- Navigation to Labour Management screen is complete
- Add Labour dialog is open

**Steps**:
1. Enter labour name: "Priya Singh"
2. Enter phone number: "9876543211"
3. Select Shift Type: "Half Day"
4. Verify hours are auto-filled to 4.0
5. Enter hourly rate: "300"
6. Select work date: (current date)
7. Enter work description: "Weeding crops"
8. Click Save

**Expected Result**:
- Labour entry is saved successfully
- Toast message shows "Labour entry added successfully"
- Entry appears in labour list with:
  - Name: "Priya Singh"
  - Phone: "9876543211"
  - Shift: "Half Day"
  - Hours: 4.0
  - Rate: ₹300/hr
  - Total Pay: ₹1200.00

**Actual Result**: ___________________

**Status**: ☐ PASS ☐ FAIL

---

### 3. Add Hourly Labour Entry (Custom Hours)

**Test ID**: T3_HOURLY_CUSTOM

**Preconditions**:
- User is logged in as a Farmer
- Navigation to Labour Management screen is complete
- Add Labour dialog is open

**Steps**:
1. Enter labour name: "Amit Patel"
2. Enter phone number: "9876543212"
3. Select Shift Type: "Hourly"
4. Verify hours field is empty
5. Enter hours: "6"
6. Enter hourly rate: "250"
7. Select work date: (current date)
8. Enter work description: "Irrigation work"
9. Click Save

**Expected Result**:
- Labour entry is saved successfully
- Toast message shows "Labour entry added successfully"
- Entry appears in labour list with:
  - Name: "Amit Patel"
  - Phone: "9876543212"
  - Shift: "Hourly"
  - Hours: 6.0
  - Rate: ₹250/hr
  - Total Pay: ₹1500.00

**Actual Result**: ___________________

**Status**: ☐ PASS ☐ FAIL

---

### 4. Shift Type Change - Full Day to Half Day

**Test ID**: T4_SHIFT_CHANGE_FD_HD

**Preconditions**:
- User is logged in as a Farmer
- Add Labour dialog is open with "Full Day" initially selected

**Steps**:
1. Enter labour name: "Test Worker"
2. Enter phone number: "9999999999"
3. Select Shift Type: "Full Day"
4. Verify hours are set to 8.0
5. Change Shift Type to: "Half Day"
6. Verify hours are auto-updated to 4.0
7. Enter hourly rate: "400"

**Expected Result**:
- Hours field automatically updates from 8.0 to 4.0 when shift type changes
- Hours field is editable
- User can proceed with the updated hours

**Actual Result**: ___________________

**Status**: ☐ PASS ☐ FAIL

---

### 5. Shift Type Change - Half Day to Hourly

**Test ID**: T5_SHIFT_CHANGE_HD_HY

**Preconditions**:
- User is logged in as a Farmer
- Add Labour dialog is open with "Half Day" initially selected

**Steps**:
1. Enter labour name: "Test Worker 2"
2. Enter phone number: "8888888888"
3. Select Shift Type: "Half Day"
4. Verify hours are set to 4.0
5. Change Shift Type to: "Hourly"
6. Verify hours field is cleared/empty
7. Enter custom hours: "5.5"

**Expected Result**:
- Hours field is cleared when changing to "Hourly"
- User can enter custom hours
- Hours value of 5.5 is accepted

**Actual Result**: ___________________

**Status**: ☐ PASS ☐ FAIL

---

### 6. Display Shift Type in Labour List

**Test ID**: T6_DISPLAY_SHIFT_TYPE

**Preconditions**:
- Multiple labour entries have been added with different shift types
- Labour Management list view is displayed

**Steps**:
1. View labour list with entries of different shift types
2. Verify each entry displays:
   - Shift type correctly (Full Day, Half Day, or Hourly)
   - Position: below phone number in the card
   - Format: "Shift: [Shift Type]"

**Expected Result**:
- All labour entries display their shift type correctly
- Format is consistent across all entries
- Shift type is readable and properly positioned

**Actual Result**: ___________________

**Status**: ☐ PASS ☐ FAIL

---

### 7. Payment Calculation with Different Shift Types

**Test ID**: T7_PAYMENT_CALC

**Preconditions**:
- Labour entries with different shift types and rates have been added

**Steps**:
1. Add Full Day entry: 8 hours × ₹500 = ₹4000
2. Add Half Day entry: 4 hours × ₹300 = ₹1200
3. Add Hourly entry: 6 hours × ₹250 = ₹1500
4. View each entry in the list

**Expected Result**:
- Total Pay calculation is correct for each entry:
  - Full Day: ₹4000.00
  - Half Day: ₹1200.00
  - Hourly: ₹1500.00
- Formula: Hours × Rate = Total Pay (regardless of shift type)

**Actual Result**: ___________________

**Status**: ☐ PASS ☐ FAIL

---

### 8. Validation - Missing Shift Type Selection

**Test ID**: T8_VALIDATION_SHIFT

**Preconditions**:
- User is logged in as a Farmer
- Add Labour dialog is open

**Steps**:
1. Enter labour name: "John"
2. Enter phone number: "9999999999"
3. Skip shift type selection (leave default or don't interact)
4. Enter hourly rate: "500"
5. Select work date
6. Enter work description: "Test"
7. Click Save

**Expected Result**:
- Default shift type "Full Day" should be selected
- Entry should save successfully with Full Day as default
- OR validation error if shift type is required

**Actual Result**: ___________________

**Status**: ☐ PASS ☐ FAIL

---

### 9. UI Responsiveness - Shift Type Dropdown

**Test ID**: T9_UI_DROPDOWN

**Preconditions**:
- Add Labour dialog is open

**Steps**:
1. Click on shift type field
2. Verify dropdown opens
3. Verify all three options are visible: Full Day, Half Day, Hourly
4. Click on different options
5. Verify selection updates and hours auto-calculate

**Expected Result**:
- Dropdown opens smoothly
- All three shift types are visible
- Clicking an option updates the selection
- No UI glitches or layout breaks

**Actual Result**: ___________________

**Status**: ☐ PASS ☐ FAIL

---

### 10. Data Persistence - Shift Type Storage

**Test ID**: T10_DATA_PERSISTENCE

**Preconditions**:
- Labour entries have been added with different shift types
- App is running with Firebase backend configured

**Steps**:
1. Add labour entries with different shift types
2. Close the app completely
3. Reopen the app
4. Navigate to Labour Management
5. Verify all entries are loaded

**Expected Result**:
- All labour entries are restored
- Shift type information is preserved for each entry
- Entries display correct shift type after reload
- No data loss occurs

**Actual Result**: ___________________

**Status**: ☐ PASS ☐ FAIL

---

## Test Summary

| Test ID | Test Name | Expected | Status |
|---------|-----------|----------|--------|
| T1_FULL_DAY | Add Full Day Labour Entry | PASS | ☐ |
| T2_HALF_DAY | Add Half Day Labour Entry | PASS | ☐ |
| T3_HOURLY_CUSTOM | Add Hourly Labour Entry | PASS | ☐ |
| T4_SHIFT_CHANGE_FD_HD | Shift Type Change (FD→HD) | PASS | ☐ |
| T5_SHIFT_CHANGE_HD_HY | Shift Type Change (HD→HY) | PASS | ☐ |
| T6_DISPLAY_SHIFT_TYPE | Display Shift Type in List | PASS | ☐ |
| T7_PAYMENT_CALC | Payment Calculation | PASS | ☐ |
| T8_VALIDATION_SHIFT | Validation - Missing Shift | PASS | ☐ |
| T9_UI_DROPDOWN | UI Responsiveness | PASS | ☐ |
| T10_DATA_PERSISTENCE | Data Persistence | PASS | ☐ |

**Total Tests**: 10  
**Pass**: ____  
**Fail**: ____  
**Success Rate**: ____%

---

## Build Test Results

**Build Date**: November 15, 2025  
**Gradle Build**: ✅ SUCCESSFUL

```
BUILD SUCCESSFUL in 1m 3s
79 actionable tasks: 78 executed, 1 up-to-date
```

**Build Details**:
- Compilation: ✅ Successful (with minor deprecation warnings)
- Resource Processing: ✅ Successful
- Asset Merging: ✅ Successful
- APK Assembly: ✅ Successful
- Lint Check: ✅ Passed

---

## Issues Found (if any)

| Issue # | Description | Severity | Status |
|---------|-------------|----------|--------|
| | | | |

---

## Sign-Off

**Tester Name**: _____________________  
**Date**: _____________________  
**Status**: ☐ APPROVED ☐ REJECTED

**Comments**:

_______________________________________________________________________________

_______________________________________________________________________________

---

## Appendix: Quick Reference

### Shift Type Auto-Calculations
- **Full Day**: 8 hours
- **Half Day**: 4 hours
- **Hourly**: User-defined hours

### Payment Formula
`Total Pay = Hours Worked × Hourly Rate`

### Database Field
- Field Name: `shiftType`
- Type: String
- Storage Format: Enum name (FULL_DAY, HALF_DAY, HOURLY)
- Default: FULL_DAY

