# GreenLedger - Technical Specification v2.0

## Document Information
- **Version**: 2.0
- **Date**: October 31, 2025
- **Status**: Implementation Phase
- **Authors**: GreenLedger Development Team

---

## Table of Contents

1. [Overview](#overview)
2. [System Architecture](#system-architecture)
3. [Role-Based Access Control](#role-based-access-control)
4. [Database Schema](#database-schema)
5. [Feature Specifications](#feature-specifications)
6. [Security Considerations](#security-considerations)
7. [API Endpoints](#api-endpoints)
8. [Implementation Plan](#implementation-plan)

---

## Overview

### Purpose
GreenLedger v2.0 is a comprehensive farm management system designed to help farmers track finances, manage operations, monitor crop lifecycles, handle labour, and generate insightful reports for better decision-making.

### Key Enhancement Areas
- **Role-Based Access Control**: Support for Farmer, Labourer, and Business Partner roles
- **Crop Lifecycle Management**: Complete tracking from sowing to harvesting
- **Advanced Financial Tracking**: Multi-mode payments, petty cash, auto-statements
- **Labour Management**: Schedules, attendance, automated payroll
- **Reporting & Analytics**: Profit/loss, charts, export capabilities

### Design Principles
1. **Security First**: Role-based permissions, data encryption
2. **User-Centric**: Intuitive UI for users with varying technical skills
3. **Scalability**: Architecture supports multiple farms and users
4. **Offline-First**: Core operations work without internet
5. **Data Integrity**: Validation at every layer

---

## System Architecture

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Presentation Layer                    │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐              │
│  │  Farmer  │  │ Labourer │  │ Business │              │
│  │   View   │  │   View   │  │ Partner  │              │
│  └──────────┘  └──────────┘  └──────────┘              │
└─────────────────────────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────┐
│                    Business Logic Layer                  │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐              │
│  │   Auth   │  │  Crops   │  │ Finance  │              │
│  │ Manager  │  │ Manager  │  │ Manager  │              │
│  └──────────┘  └──────────┘  └──────────┘              │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐              │
│  │  Labour  │  │ Storage  │  │ Reports  │              │
│  │ Manager  │  │ Manager  │  │ Manager  │              │
│  └──────────┘  └──────────┘  └──────────┘              │
└─────────────────────────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────┐
│                      Data Layer                          │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐              │
│  │ Firebase │  │  Local   │  │  Cache   │              │
│  │ Realtime │  │  SQLite  │  │  Layer   │              │
│  │ Database │  │ Database │  │          │              │
│  └──────────┘  └──────────┘  └──────────┘              │
└─────────────────────────────────────────────────────────┘
```

### Technology Stack Updates

| Component | Technology | Version | Purpose |
|-----------|-----------|---------|---------|
| Database (Online) | Firebase Realtime Database | Latest | Primary data store |
| Database (Offline) | Room (SQLite) | 2.6.0 | Offline cache |
| Authentication | Firebase Auth | 32.7.0 | User authentication |
| PDF Generation | iText | 7.2.5 | Report generation |
| Charts | MPAndroidChart | 3.1.0 | Data visualization |
| Excel Export | Apache POI | 5.2.5 | Excel report generation |
| Image Handling | Glide | 4.16.0 | Image loading/caching |

---

## Role-Based Access Control

### User Roles

#### 1. Farmer (Landlord)
**Primary Role**: Owner and decision maker

**Permissions**:
- ✅ Full access to all features
- ✅ View/Edit/Delete all data
- ✅ Manage labourers and business partners
- ✅ Create farms and assign lands
- ✅ Generate all reports
- ✅ Financial transactions (all types)
- ✅ Configure system settings

**Use Cases**:
- Track all farm operations
- Monitor crop lifecycle
- Manage finances (expenses, revenue)
- Oversee labour work and payments
- Analyze profitability
- Make strategic decisions

#### 2. Labourer
**Primary Role**: Worker

**Permissions**:
- ✅ View assigned tasks/schedules
- ✅ Log work hours (check-in/check-out)
- ✅ View payment history
- ✅ Update work status
- ❌ Cannot modify financial data
- ❌ Cannot access other labourers' data
- ❌ Cannot generate reports

**Use Cases**:
- Check daily work schedule
- Mark attendance
- Log work hours
- View payment statements
- Update task completion status

#### 3. Business Partner
**Primary Role**: Buyer/Supplier

**Permissions**:
- ✅ View crop inventory (sold/unsold)
- ✅ Place orders for crops
- ✅ View transaction history
- ✅ Communication with farmer
- ❌ Cannot view farm operations
- ❌ Cannot access labour data
- ❌ Cannot modify crop data

**Use Cases**:
- Browse available crops
- Place purchase orders
- Track delivery status
- View payment history
- Negotiate prices

### Access Control Implementation

```java
public enum UserRole {
    FARMER("Farmer", 100),           // Full access
    LABOURER("Labourer", 50),        // Limited access
    BUSINESS_PARTNER("Business Partner", 30); // Read-only for specific data

    private final String displayName;
    private final int accessLevel;
}

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
}
```

---

## Database Schema

### Enhanced Schema Design

#### 1. Users Collection
```json
{
  "userId": "string (Firebase UID)",
  "personalInfo": {
    "name": "string",
    "phone": "string",
    "email": "string (optional)",
    "role": "Farmer|Labourer|Business Partner",
    "profilePhoto": "string (URL)",
    "address": {
      "street": "string",
      "village": "string",
      "district": "string",
      "state": "string",
      "pincode": "string"
    }
  },
  "bankDetails": {
    "accountHolderName": "string",
    "accountNumber": "string (encrypted)",
    "ifscCode": "string",
    "bankName": "string",
    "branch": "string",
    "upiId": "string (optional)"
  },
  "preferences": {
    "language": "string",
    "currency": "INR",
    "notifications": "boolean"
  },
  "metadata": {
    "createdAt": "timestamp",
    "lastLogin": "timestamp",
    "isActive": "boolean"
  }
}
```

#### 2. Farms Collection
```json
{
  "farmId": "string (auto-generated)",
  "farmerId": "string (owner userId)",
  "farmDetails": {
    "name": "string",
    "totalArea": "number (acres)",
    "location": {
      "latitude": "number",
      "longitude": "number",
      "address": "string"
    },
    "soilType": "string",
    "irrigationType": "string (Drip|Sprinkler|Flood)"
  },
  "lands": [
    {
      "landId": "string",
      "name": "string (e.g., 'North Field')",
      "area": "number (acres)",
      "currentCrop": "string (cropId)",
      "status": "Idle|Occupied|Fallow"
    }
  ],
  "metadata": {
    "createdAt": "timestamp",
    "updatedAt": "timestamp"
  }
}
```

#### 3. Crops Collection
```json
{
  "cropId": "string (auto-generated)",
  "farmId": "string",
  "farmerId": "string",
  "landId": "string",
  "cropInfo": {
    "type": "string (Rice|Wheat|Cotton|Sugarcane|etc)",
    "variety": "string",
    "category": "Kharif|Rabi|Zaid"
  },
  "lifecycle": {
    "status": "Planning|Sowing|Growing|Harvesting|Completed",
    "stages": [
      {
        "stage": "Sowing|Plowing|Irrigation|Fertilizing|Harvesting|Reaping",
        "startDate": "timestamp",
        "endDate": "timestamp",
        "completedBy": "userId (labourer)",
        "notes": "string",
        "photos": ["string (URLs)"]
      }
    ],
    "sowingDate": "timestamp",
    "expectedHarvestDate": "timestamp",
    "actualHarvestDate": "timestamp"
  },
  "quantity": {
    "expected": "number (kg/quintal)",
    "actual": "number",
    "unit": "kg|quintal|ton"
  },
  "quality": {
    "grade": "A|B|C",
    "moistureLevel": "number (%)",
    "purityLevel": "number (%)"
  },
  "inventory": {
    "total": "number",
    "sold": "number",
    "unsold": "number",
    "spoiled": "number",
    "reserved": "number"
  },
  "storage": {
    "storageId": "string",
    "location": "string",
    "dateStored": "timestamp",
    "condition": "Good|Fair|Poor"
  },
  "financial": {
    "costOfProduction": "number",
    "sellingPrice": "number (per unit)",
    "totalRevenue": "number",
    "profitLoss": "number"
  },
  "metadata": {
    "createdAt": "timestamp",
    "updatedAt": "timestamp",
    "createdBy": "userId"
  }
}
```

#### 4. Storage Collection
```json
{
  "storageId": "string (auto-generated)",
  "farmerId": "string",
  "storageInfo": {
    "name": "string (Barn A|Warehouse 1)",
    "type": "Barn|Warehouse|Cold Storage|Open Storage",
    "capacity": "number (quintals)",
    "currentOccupancy": "number",
    "location": "string"
  },
  "inventory": [
    {
      "cropId": "string",
      "quantity": "number",
      "dateStored": "timestamp",
      "expiryDate": "timestamp (optional)",
      "condition": "string"
    }
  ],
  "metadata": {
    "createdAt": "timestamp",
    "updatedAt": "timestamp"
  }
}
```

#### 5. Enhanced Expenses Collection
```json
{
  "expenseId": "string",
  "farmerId": "string",
  "farmId": "string (optional)",
  "cropId": "string (optional)",
  "expenseInfo": {
    "category": "Seeds|Fertilizers|Pesticides|Labour|Equipment|Transportation|Utilities|Other",
    "subcategory": "string",
    "amount": "number",
    "quantity": "number (optional)",
    "unit": "string (optional)",
    "description": "string"
  },
  "payment": {
    "mode": "Cash|Online|Cheque|UPI",
    "status": "Paid|Pending|Partial",
    "paidAmount": "number",
    "pendingAmount": "number",
    "transactionId": "string (optional)",
    "paymentDate": "timestamp"
  },
  "vendor": {
    "vendorId": "string (optional)",
    "name": "string",
    "contact": "string"
  },
  "attachments": ["string (URLs for bills/receipts)"],
  "metadata": {
    "date": "timestamp",
    "createdAt": "timestamp",
    "createdBy": "userId"
  }
}
```

#### 6. Enhanced Labour Collection
```json
{
  "labourEntryId": "string",
  "labourerId": "string",
  "farmerId": "string",
  "farmId": "string",
  "workDetails": {
    "date": "timestamp",
    "checkIn": "timestamp",
    "checkOut": "timestamp",
    "workType": "Plowing|Sowing|Irrigation|Harvesting|Maintenance|Other",
    "cropId": "string (optional)",
    "landId": "string (optional)",
    "shiftType": "Full Day|Half Day|Hourly",
    "hoursWorked": "number",
    "overtimeHours": "number"
  },
  "payment": {
    "rateType": "Daily|Hourly|Contract",
    "baseRate": "number",
    "overtimeRate": "number",
    "totalAmount": "number",
    "status": "Pending|Paid|Partial",
    "paidAmount": "number",
    "paymentMode": "Cash|Online|UPI",
    "paymentDate": "timestamp",
    "transactionId": "string"
  },
  "performance": {
    "rating": "number (1-5)",
    "feedback": "string",
    "workQuality": "Excellent|Good|Average|Poor"
  },
  "metadata": {
    "timestamp": "timestamp",
    "createdBy": "userId"
  }
}
```

#### 7. Sales/Revenue Collection
```json
{
  "saleId": "string",
  "farmerId": "string",
  "cropId": "string",
  "buyer": {
    "buyerId": "string (businessPartner userId)",
    "name": "string",
    "contact": "string",
    "company": "string (optional)"
  },
  "saleDetails": {
    "quantity": "number",
    "unit": "string",
    "pricePerUnit": "number",
    "totalAmount": "number",
    "discount": "number (optional)",
    "finalAmount": "number"
  },
  "payment": {
    "mode": "Cash|Online|Cheque|UPI|Credit",
    "status": "Paid|Pending|Partial",
    "paidAmount": "number",
    "pendingAmount": "number",
    "dueDate": "timestamp (if credit)",
    "transactions": [
      {
        "amount": "number",
        "date": "timestamp",
        "mode": "string",
        "transactionId": "string"
      }
    ]
  },
  "delivery": {
    "status": "Pending|In Transit|Delivered",
    "deliveryDate": "timestamp",
    "vehicle": "string",
    "driverName": "string",
    "driverContact": "string",
    "distance": "number (km)",
    "transportationCost": "number"
  },
  "metadata": {
    "saleDate": "timestamp",
    "createdAt": "timestamp",
    "invoiceNumber": "string"
  }
}
```

#### 8. Schedules Collection (For Labour Management)
```json
{
  "scheduleId": "string",
  "farmerId": "string",
  "farmId": "string",
  "scheduleInfo": {
    "title": "string",
    "date": "timestamp",
    "startTime": "timestamp",
    "endTime": "timestamp",
    "workType": "string",
    "priority": "High|Medium|Low"
  },
  "assignedLabourers": [
    {
      "labourerId": "string",
      "name": "string",
      "status": "Assigned|Confirmed|Completed|Absent"
    }
  ],
  "cropId": "string (optional)",
  "landId": "string (optional)",
  "instructions": "string",
  "metadata": {
    "createdAt": "timestamp",
    "createdBy": "userId"
  }
}
```

#### 9. Petty Cash Collection
```json
{
  "pettyCashId": "string",
  "farmerId": "string",
  "transaction": {
    "type": "Income|Expense",
    "amount": "number",
    "category": "string",
    "description": "string",
    "date": "timestamp"
  },
  "balance": {
    "openingBalance": "number",
    "closingBalance": "number"
  },
  "handledBy": "userId",
  "metadata": {
    "createdAt": "timestamp"
  }
}
```

#### 10. Business Partners Collection
```json
{
  "partnerId": "string (userId)",
  "farmerId": "string (linked farmer)",
  "partnerInfo": {
    "businessName": "string",
    "type": "Buyer|Supplier|Both",
    "gstNumber": "string (optional)",
    "panNumber": "string (optional)"
  },
  "preferences": {
    "preferredCrops": ["string"],
    "paymentTerms": "Immediate|7Days|15Days|30Days",
    "minimumOrder": "number"
  },
  "transactionHistory": {
    "totalPurchases": "number",
    "totalAmount": "number",
    "lastPurchaseDate": "timestamp",
    "outstandingAmount": "number"
  },
  "metadata": {
    "relationship": "string",
    "rating": "number (1-5)",
    "notes": "string",
    "createdAt": "timestamp"
  }
}
```

---

## Feature Specifications

### 1. Enhanced User Registration

**Fields**:
- Personal: Name, Phone, Email, Role, Address, Photo
- Banking: Account details, IFSC, UPI (optional)
- Preferences: Language, Currency

**Validation**:
- Phone: 10 digits, unique
- Bank Account: Encrypted storage
- Role: Required selection

**Security**:
- Bank details encrypted using AES-256
- PII data access logged

### 2. Crop Lifecycle Management

**Stages**:
1. **Planning**: Select crop type, variety, land
2. **Sowing**: Record sowing date, seed quantity
3. **Growing**: Track irrigation, fertilization, pest control
4. **Harvesting**: Log harvest date, quantity
5. **Post-Harvest**: Storage, quality checks

**Features**:
- Photo documentation at each stage
- Labourer assignment per stage
- Cost tracking per stage
- Expected vs actual comparison
- Weather integration (future)

### 3. Storage Management

**Capabilities**:
- Multiple storage locations per farm
- Capacity tracking
- Inventory movement logs
- Spoilage alerts
- Storage cost allocation

### 4. Advanced Financial Tracking

**Payment Modes**:
- Cash
- Online (UPI, Net Banking)
- Cheque
- Credit/Advance

**Features**:
- Multi-currency support
- Petty cash register
- Expense categorization
- Payment reminders
- Auto-reconciliation
- Tax calculation helpers

### 5. Labour Management System

**Attendance**:
- Check-in/Check-out via app
- GPS location tracking
- Photo verification (optional)
- Shift management

**Payroll**:
- Multiple rate types (daily/hourly/contract)
- Overtime calculation
- Deductions support
- Auto-generated payslips
- Payment history

**Scheduling**:
- Calendar view
- Task assignment
- Shift management
- Conflict detection

### 6. Reporting & Analytics

**Reports**:
1. **Profit & Loss Statement**
   - Period-based (monthly/quarterly/yearly)
   - Crop-wise breakdown
   - Export to PDF/Excel

2. **Expense Analysis**
   - Category-wise charts
   - Trend analysis
   - Budget vs actual

3. **Labour Report**
   - Attendance summary
   - Payment analysis
   - Performance metrics

4. **Crop Yield Report**
   - Expected vs actual
   - Quality analysis
   - Revenue breakdown

5. **Cash Flow Statement**
   - Inflow/outflow analysis
   - Bank balance tracking
   - Petty cash reconciliation

**Analytics**:
- Interactive charts (Line, Bar, Pie)
- Comparison tools
- Forecasting (ML-based, future)
- Dashboard KPIs

---

## Security Considerations

### Data Security
1. **Encryption**
   - Bank details: AES-256
   - Sensitive PII: Field-level encryption
   - Network: TLS 1.3

2. **Access Control**
   - Role-based permissions
   - Firebase security rules
   - Session management

3. **Audit Trail**
   - All financial transactions logged
   - User action tracking
   - Data modification history

### Privacy
- GDPR-like compliance
- User consent for data sharing
- Right to delete account
- Data export capability

---

## Implementation Plan

### Phase 1: Foundation (Week 1-2)
- [ ] Enhanced database schema
- [ ] Role-based access control
- [ ] User profile enhancements
- [ ] Authentication updates

### Phase 2: Core Features (Week 3-4)
- [ ] Crop lifecycle management
- [ ] Storage management
- [ ] Enhanced financial tracking
- [ ] Labour management v2

### Phase 3: Advanced Features (Week 5-6)
- [ ] Scheduling system
- [ ] Petty cash module
- [ ] Business partner integration
- [ ] Offline sync

### Phase 4: Analytics (Week 7-8)
- [ ] Report generation
- [ ] Charts and visualizations
- [ ] Export functionality
- [ ] Dashboard KPIs

### Phase 5: Testing & Deployment (Week 9-10)
- [ ] Comprehensive testing
- [ ] Performance optimization
- [ ] Documentation
- [ ] Production deployment

---

## Conclusion

This technical specification provides a comprehensive roadmap for GreenLedger v2.0. The enhanced system will support farmers in making data-driven decisions, managing operations efficiently, and maintaining financial transparency.

**Next Steps**:
1. Review and approval
2. Database migration plan
3. UI/UX mockups
4. Sprint planning

---

**Document Control**
- Last Updated: October 31, 2025
- Next Review: November 15, 2025
- Approved By: Development Team
