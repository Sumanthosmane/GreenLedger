# GreenLedger v2.0 - User Testing Guide

## Overview
This document provides a comprehensive checklist for user testing of GreenLedger v2.0. All test cases should be performed on at least 3 different Android devices covering API levels 24-34.

## Test Documentation Template
For each test case, document:
- Test Date
- Device Model
- Android Version
- Test Result (Pass/Fail)
- Screenshots (if issues found)
- Steps to Reproduce (if failed)
- Tester Notes

---

## 1. Authentication Testing

### 1.1 User Registration
| Test Case | Steps | Expected Result | Status |
|-----------|-------|-----------------|--------|
| New Registration | 1. Enter phone number<br>2. Enter password<br>3. Select role<br>4. Enter bank details<br>5. Upload profile photo | Account created successfully | □ |
| Duplicate Phone | Try registering with existing number | Error message shown | □ |
| Password Validation | Try weak passwords | Proper validation messages | □ |
| Role Selection | Test each role type | Correct role-specific UI shown | □ |
| Bank Details | Enter and verify encryption | Data stored securely | □ |
| Required Fields | Skip mandatory fields | Validation errors shown | □ |
| Photo Upload | Test different image sizes | Photo uploaded and compressed | □ |

### 1.2 Login Flow
| Test Case | Steps | Expected Result | Status |
|-----------|-------|-----------------|--------|
| Valid Login | Enter correct credentials | Login successful | □ |
| Invalid Password | Try incorrect password | Error message shown | □ |
| Remember Me | Check remember me and restart | Auto-login works | □ |
| Session Management | Test app background/foreground | Session maintained properly | □ |
| Logout | Test logout function | All data cleared properly | □ |
| Session Expiry | Leave app idle | Proper expiry handling | □ |

---

## 2. Farm Management

### 2.1 Farm Creation
| Test Case | Steps | Expected Result | Status |
|-----------|-------|-----------------|--------|
| New Farm | Add complete farm details | Farm created successfully | □ |
| Land Plots | Add multiple land plots | Plots added correctly | □ |
| Area Calculation | Enter plot dimensions | Accurate calculations | □ |
| Soil Types | Select different soil types | Stored correctly | □ |
| Irrigation | Test irrigation options | Options saved properly | □ |
| Multiple Farms | Create several farms | All farms visible and managed | □ |

### 2.2 Land Management
| Test Case | Steps | Expected Result | Status |
|-----------|-------|-----------------|--------|
| Plot Addition | Add new land plot | Plot created successfully | □ |
| Area Validation | Enter invalid areas | Proper validation shown | □ |
| Crop Assignment | Assign crops to plots | Assignments saved | □ |
| Status Updates | Change plot status | Status updated correctly | □ |
| Plot Deletion | Delete existing plot | Plot removed properly | □ |

---

## 3. Crop Management

### 3.1 Lifecycle Testing
| Test Case | Steps | Expected Result | Status |
|-----------|-------|-----------------|--------|
| New Crop | Create crop entry | Crop created successfully | □ |
| Planning Stage | Enter planning details | Details saved properly | □ |
| Sowing Stage | Update to sowing | Stage updated correctly | □ |
| Growing Stage | Track growth progress | Progress saved properly | □ |
| Harvesting | Record harvest details | Details captured correctly | □ |
| Photo Upload | Add photos per stage | Photos stored properly | □ |
| Progress Update | Update stage progress | Progress tracked correctly | □ |
| Quality Grading | Enter quality metrics | Grades saved properly | □ |

---

## 4. Labour Management

### 4.1 Attendance
| Test Case | Steps | Expected Result | Status |
|-----------|-------|-----------------|--------|
| Check-in | Record worker arrival | Time recorded correctly | □ |
| Check-out | Record worker departure | Hours calculated properly | □ |
| Overtime | Track overtime hours | OT calculated correctly | □ |
| Shift Types | Test different shifts | Shifts recorded properly | □ |
| GPS Tracking | Verify location capture | Location recorded accurately | □ |

### 4.2 Payroll
| Test Case | Steps | Expected Result | Status |
|-----------|-------|-----------------|--------|
| Wage Calculation | Calculate daily wages | Amount calculated correctly | □ |
| Overtime Pay | Calculate OT payment | OT pay calculated properly | □ |
| Payment Processing | Process payments | Payments recorded correctly | □ |
| Multiple Modes | Test payment methods | All modes working properly | □ |
| History | View payment history | History displayed correctly | □ |
| Payslips | Generate payslips | PDFs generated properly | □ |

---

## 5. Financial Management

### 5.1 Expense Tracking
| Test Case | Steps | Expected Result | Status |
|-----------|-------|-----------------|--------|
| Add Expense | Enter new expense | Expense recorded properly | □ |
| Categories | Test all categories | Categorized correctly | □ |
| Receipts | Upload receipt images | Images stored properly | □ |
| Budget Track | Check budget updates | Budget updated correctly | □ |
| Payment Modes | Test all modes | Modes working properly | □ |

### 5.2 Revenue Management
| Test Case | Steps | Expected Result | Status |
|-----------|-------|-----------------|--------|
| Sales Record | Record crop sale | Sale recorded properly | □ |
| Partial Payments | Record partial payment | Payment tracked correctly | □ |
| Invoice Generation | Generate invoice | PDF created properly | □ |
| Payment Tracking | Track pending payments | Status updated correctly | □ |

### 5.3 Petty Cash
| Test Case | Steps | Expected Result | Status |
|-----------|-------|-----------------|--------|
| Cash Entry | Add cash transaction | Transaction recorded | □ |
| Balance Check | Verify calculations | Balance correct | □ |
| Reconciliation | Perform reconciliation | Matches correctly | □ |
| Reports | Generate cash reports | Reports accurate | □ |

---

## 6. Storage Management

### 6.1 Inventory
| Test Case | Steps | Expected Result | Status |
|-----------|-------|-----------------|--------|
| Add Storage | Create storage facility | Facility added properly | □ |
| Track Crops | Add crops to storage | Tracking working properly | □ |
| Capacity | Check capacity limits | Limits enforced properly | □ |
| Movement | Record stock movement | Movement tracked properly | □ |
| Spoilage | Test spoilage alerts | Alerts working properly | □ |
| Quality Check | Record quality checks | Quality tracked properly | □ |

---

## 7. Reporting

### 7.1 Report Generation
| Test Case | Steps | Expected Result | Status |
|-----------|-------|-----------------|--------|
| P&L Report | Generate P&L report | Report created properly | □ |
| Expense Report | Create expense analysis | Analysis accurate | □ |
| Labour Report | Generate labour report | Data accurate | □ |
| Yield Report | Create yield report | Calculations correct | □ |
| Storage Report | Generate storage report | Data accurate | □ |
| Cash Flow | Create cash flow report | Calculations correct | □ |
| Date Filters | Test date ranges | Filters working properly | □ |
| Export PDF | Export to PDF | PDF created properly | □ |
| Export Excel | Export to Excel | Excel created properly | □ |
| Charts | Generate all charts | Charts rendered properly | □ |

---

## 8. Offline Functionality
| Test Case | Steps | Expected Result | Status |
|-----------|-------|-----------------|--------|
| No Internet | Use app offline | Functions working properly | □ |
| Data Sync | Restore connection | Data synced correctly | □ |
| Conflicts | Create sync conflict | Resolved properly | □ |
| Storage Limit | Check offline limits | Limits handled properly | □ |
| Notifications | Check sync notifications | Notifications working | □ |

---

## 9. Performance Testing
| Test Case | Steps | Expected Result | Status |
|-----------|-------|-----------------|--------|
| App Launch | Time app startup | Under 3 seconds | □ |
| Navigation | Check screen transitions | Smooth transitions | □ |
| Image Loading | Load multiple images | Quick loading | □ |
| Report Speed | Generate large reports | Quick generation | □ |
| Sync Speed | Sync large dataset | Reasonable speed | □ |
| Memory Usage | Monitor memory use | Within limits | □ |
| Battery Use | Monitor battery drain | Reasonable usage | □ |

---

## 10. Security Testing
| Test Case | Steps | Expected Result | Status |
|-----------|-------|-----------------|--------|
| Role Access | Test role restrictions | Properly restricted | □ |
| Encryption | Verify sensitive data | Properly encrypted | □ |
| Session | Check session handling | Properly managed | □ |
| Data Export | Test secure export | Properly secured | □ |
| Bank Data | Check bank details | Properly protected | □ |
| Tokens | Verify access tokens | Properly handled | □ |

---

## 11. Cross-Device Testing
| Test Case | Steps | Expected Result | Status |
|-----------|-------|-----------------|--------|
| API 24 | Test on Android 7 | Working properly | □ |
| API 34 | Test on Android 14 | Working properly | □ |
| Tablets | Test tablet layout | Layout correct | □ |
| Screen Sizes | Test various sizes | Adapts properly | □ |
| Orientation | Test rotation | Handles properly | □ |

---

## 12. Error Handling
| Test Case | Steps | Expected Result | Status |
|-----------|-------|-----------------|--------|
| Network Error | Test network issues | Proper error handling | □ |
| Invalid Input | Test bad inputs | Proper validation | □ |
| Sync Error | Create sync issues | Proper resolution | □ |
| Storage Full | Fill device storage | Proper handling | □ |
| Firebase Error | Test Firebase issues | Proper handling | □ |
| Upload Error | Test upload failures | Proper handling | □ |

---

## 13. Usability Testing
| Test Case | Steps | Expected Result | Status |
|-----------|-------|-----------------|--------|
| Navigation | Test app navigation | Intuitive flow | □ |
| Validation | Test form validation | Clear messages | □ |
| Error Messages | Check error display | Clear and helpful | □ |
| Loading States | Check loading indicators | Properly shown | □ |
| Feedback | Check user feedback | Clear feedback | □ |
| Date Picker | Test date selection | Easy to use | □ |
| Search | Test search function | Works properly | □ |
| Filters | Test data filters | Work properly | □ |

---

## Test Execution Log

| Date | Tester | Device | Android Ver | Test Section | Result | Notes |
|------|--------|---------|-------------|--------------|--------|-------|
|      |        |         |             |              |        |       |

---

## Document Control
- **Version**: 1.0
- **Created**: November 9, 2025
- **Last Updated**: November 9, 2025
- **Status**: Ready for Testing
- **Next Review**: November 23, 2025
