# GreenLedger Database Schema v2.0

## Overview

This document provides the complete database schema for GreenLedger v2.0, including all collections, fields, relationships, and Firebase security rules.

---

## Schema Diagram

```
Users
  ├─ Farms
  │   ├─ Lands
  │   └─ Crops
  │       └─ CropStages
  ├─ Storage
  │   └─ StorageInventory
  ├─ Expenses
  ├─ Labour
  ├─ Sales
  ├─ Schedules
  ├─ PettyCash
  └─ BusinessPartners
```

---

## Collection Details

### 1. Users
**Path**: `/users/{userId}`

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| userId | String | Yes | Firebase UID |
| personalInfo.name | String | Yes | Full name |
| personalInfo.phone | String | Yes | Unique 10-digit phone |
| personalInfo.email | String | No | Email address |
| personalInfo.role | Enum | Yes | Farmer, Labourer, Business Partner |
| personalInfo.profilePhoto | String | No | URL to profile image |
| personalInfo.address.* | Object | No | Complete address |
| bankDetails.* | Object | No | Encrypted bank details |
| preferences.* | Object | Yes | User preferences |
| metadata.createdAt | Timestamp | Yes | Registration date |
| metadata.lastLogin | Timestamp | Yes | Last login timestamp |
| metadata.isActive | Boolean | Yes | Account status |

**Indexes**: phone, role, isActive

---

### 2. Farms
**Path**: `/farms/{farmId}`

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| farmId | String | Yes | Auto-generated |
| farmerId | String | Yes | Owner user ID |
| farmDetails.name | String | Yes | Farm name |
| farmDetails.totalArea | Number | Yes | Total area in acres |
| farmDetails.location.* | Object | Yes | GPS and address |
| farmDetails.soilType | String | No | Soil type |
| farmDetails.irrigationType | String | No | Irrigation method |
| lands | Array | No | Array of land objects |
| metadata.* | Object | Yes | Timestamps |

**Indexes**: farmerId

**Relationships**:
- farmerId → users.userId

---

### 3. Crops
**Path**: `/crops/{cropId}`

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| cropId | String | Yes | Auto-generated |
| farmId | String | Yes | Associated farm |
| farmerId | String | Yes | Farm owner |
| landId | String | Yes | Specific land plot |
| cropInfo.type | String | Yes | Crop type |
| cropInfo.variety | String | No | Crop variety |
| cropInfo.category | Enum | Yes | Kharif/Rabi/Zaid |
| lifecycle.status | Enum | Yes | Current stage |
| lifecycle.stages | Array | Yes | Stage history |
| quantity.* | Object | Yes | Expected/actual quantities |
| quality.* | Object | No | Quality metrics |
| inventory.* | Object | Yes | Stock tracking |
| storage.* | Object | No | Storage details |
| financial.* | Object | Yes | Cost and revenue |
| metadata.* | Object | Yes | Timestamps |

**Indexes**: farmId, farmerId, lifecycle.status, landId

**Relationships**:
- farmId → farms.farmId
- farmerId → users.userId
- storage.storageId → storage.storageId

---

### 4. Storage
**Path**: `/storage/{storageId}`

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| storageId | String | Yes | Auto-generated |
| farmerId | String | Yes | Owner user ID |
| storageInfo.name | String | Yes | Storage name |
| storageInfo.type | Enum | Yes | Barn/Warehouse/Cold Storage |
| storageInfo.capacity | Number | Yes | Max capacity in quintals |
| storageInfo.currentOccupancy | Number | Yes | Current usage |
| storageInfo.location | String | Yes | Physical location |
| inventory | Array | Yes | Stored crops |
| metadata.* | Object | Yes | Timestamps |

**Indexes**: farmerId

**Relationships**:
- farmerId → users.userId

---

### 5. Expenses
**Path**: `/expenses/{expenseId}`

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| expenseId | String | Yes | Auto-generated |
| farmerId | String | Yes | Expense owner |
| farmId | String | No | Related farm |
| cropId | String | No | Related crop |
| expenseInfo.category | Enum | Yes | Expense category |
| expenseInfo.subcategory | String | No | Subcategory |
| expenseInfo.amount | Number | Yes | Total amount |
| expenseInfo.description | String | Yes | Details |
| payment.mode | Enum | Yes | Cash/Online/UPI/Cheque |
| payment.status | Enum | Yes | Paid/Pending/Partial |
| payment.paidAmount | Number | Yes | Amount paid |
| payment.pendingAmount | Number | Yes | Amount pending |
| vendor.* | Object | No | Vendor details |
| attachments | Array | No | Bill/receipt URLs |
| metadata.* | Object | Yes | Timestamps |

**Indexes**: farmerId, farmId, cropId, expenseInfo.category, metadata.date

**Relationships**:
- farmerId → users.userId
- farmId → farms.farmId
- cropId → crops.cropId

---

### 6. Labour
**Path**: `/labour/{labourEntryId}`

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| labourEntryId | String | Yes | Auto-generated |
| labourerId | String | Yes | Worker user ID |
| farmerId | String | Yes | Farm owner |
| farmId | String | Yes | Work location |
| workDetails.date | Timestamp | Yes | Work date |
| workDetails.checkIn | Timestamp | Yes | Start time |
| workDetails.checkOut | Timestamp | No | End time |
| workDetails.workType | String | Yes | Type of work |
| workDetails.shiftType | Enum | Yes | Full/Half/Hourly |
| workDetails.hoursWorked | Number | Yes | Total hours |
| payment.rateType | Enum | Yes | Daily/Hourly/Contract |
| payment.baseRate | Number | Yes | Rate amount |
| payment.totalAmount | Number | Yes | Total payment |
| payment.status | Enum | Yes | Pending/Paid/Partial |
| performance.* | Object | No | Rating and feedback |
| metadata.* | Object | Yes | Timestamps |

**Indexes**: labourerId, farmerId, farmId, workDetails.date, payment.status

**Relationships**:
- labourerId → users.userId
- farmerId → users.userId
- farmId → farms.farmId

---

### 7. Sales
**Path**: `/sales/{saleId}`

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| saleId | String | Yes | Auto-generated |
| farmerId | String | Yes | Seller |
| cropId | String | Yes | Sold crop |
| buyer.buyerId | String | No | Buyer user ID |
| buyer.name | String | Yes | Buyer name |
| saleDetails.quantity | Number | Yes | Amount sold |
| saleDetails.pricePerUnit | Number | Yes | Unit price |
| saleDetails.totalAmount | Number | Yes | Total sale value |
| payment.mode | Enum | Yes | Payment method |
| payment.status | Enum | Yes | Payment status |
| payment.transactions | Array | No | Payment history |
| delivery.* | Object | No | Delivery details |
| metadata.* | Object | Yes | Timestamps |

**Indexes**: farmerId, cropId, payment.status, metadata.saleDate

**Relationships**:
- farmerId → users.userId
- cropId → crops.cropId
- buyer.buyerId → users.userId

---

### 8. Schedules
**Path**: `/schedules/{scheduleId}`

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| scheduleId | String | Yes | Auto-generated |
| farmerId | String | Yes | Schedule creator |
| farmId | String | Yes | Farm location |
| scheduleInfo.title | String | Yes | Schedule title |
| scheduleInfo.date | Timestamp | Yes | Schedule date |
| scheduleInfo.startTime | Timestamp | Yes | Start time |
| scheduleInfo.endTime | Timestamp | Yes | End time |
| scheduleInfo.workType | String | Yes | Type of work |
| scheduleInfo.priority | Enum | Yes | High/Medium/Low |
| assignedLabourers | Array | Yes | Worker assignments |
| cropId | String | No | Related crop |
| landId | String | No | Related land |
| instructions | String | No | Work instructions |
| metadata.* | Object | Yes | Timestamps |

**Indexes**: farmerId, farmId, scheduleInfo.date

**Relationships**:
- farmerId → users.userId
- farmId → farms.farmId
- assignedLabourers[].labourerId → users.userId

---

### 9. PettyCash
**Path**: `/pettyCash/{pettyCashId}`

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| pettyCashId | String | Yes | Auto-generated |
| farmerId | String | Yes | Owner |
| transaction.type | Enum | Yes | Income/Expense |
| transaction.amount | Number | Yes | Amount |
| transaction.category | String | Yes | Category |
| transaction.description | String | Yes | Details |
| transaction.date | Timestamp | Yes | Transaction date |
| balance.openingBalance | Number | Yes | Opening balance |
| balance.closingBalance | Number | Yes | Closing balance |
| handledBy | String | Yes | User who handled |
| metadata.* | Object | Yes | Timestamps |

**Indexes**: farmerId, transaction.date, transaction.type

**Relationships**:
- farmerId → users.userId
- handledBy → users.userId

---

### 10. BusinessPartners
**Path**: `/businessPartners/{partnerId}`

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| partnerId | String | Yes | User ID |
| farmerId | String | Yes | Linked farmer |
| partnerInfo.businessName | String | Yes | Business name |
| partnerInfo.type | Enum | Yes | Buyer/Supplier/Both |
| partnerInfo.gstNumber | String | No | GST number |
| preferences.* | Object | No | Business preferences |
| transactionHistory.* | Object | Yes | Transaction stats |
| metadata.* | Object | Yes | Timestamps |

**Indexes**: farmerId, partnerInfo.type

**Relationships**:
- partnerId → users.userId
- farmerId → users.userId

---

## Firebase Security Rules

```json
{
  "rules": {
    "users": {
      "$uid": {
        ".read": "$uid === auth.uid || root.child('users/' + auth.uid + '/personalInfo/role').val() === 'Farmer'",
        ".write": "$uid === auth.uid",
        "bankDetails": {
          ".read": "$uid === auth.uid"
        }
      }
    },
    "farms": {
      ".indexOn": ["farmerId"],
      "$farmId": {
        ".read": "data.child('farmerId').val() === auth.uid || root.child('users/' + auth.uid + '/personalInfo/role').val() === 'Farmer'",
        ".write": "data.child('farmerId').val() === auth.uid || !data.exists()"
      }
    },
    "crops": {
      ".indexOn": ["farmId", "farmerId", "lifecycle/status"],
      "$cropId": {
        ".read": "data.child('farmerId').val() === auth.uid || root.child('users/' + auth.uid + '/personalInfo/role').val() === 'Business Partner'",
        ".write": "data.child('farmerId').val() === auth.uid"
      }
    },
    "storage": {
      ".indexOn": ["farmerId"],
      "$storageId": {
        ".read": "data.child('farmerId').val() === auth.uid",
        ".write": "data.child('farmerId').val() === auth.uid"
      }
    },
    "expenses": {
      ".indexOn": ["farmerId", "farmId", "metadata/date"],
      "$expenseId": {
        ".read": "data.child('farmerId').val() === auth.uid",
        ".write": "data.child('farmerId').val() === auth.uid",
        ".validate": "newData.child('farmerId').val() === auth.uid"
      }
    },
    "labour": {
      ".indexOn": ["labourerId", "farmerId", "workDetails/date"],
      "$labourEntryId": {
        ".read": "data.child('labourerId').val() === auth.uid || data.child('farmerId').val() === auth.uid",
        ".write": "data.child('farmerId').val() === auth.uid || (data.child('labourerId').val() === auth.uid && newData.child('workDetails/checkOut').exists())"
      }
    },
    "sales": {
      ".indexOn": ["farmerId", "metadata/saleDate"],
      "$saleId": {
        ".read": "data.child('farmerId').val() === auth.uid || data.child('buyer/buyerId').val() === auth.uid",
        ".write": "data.child('farmerId').val() === auth.uid"
      }
    },
    "schedules": {
      ".indexOn": ["farmerId", "scheduleInfo/date"],
      "$scheduleId": {
        ".read": "data.child('farmerId').val() === auth.uid || data.child('assignedLabourers').hasChildren() && data.child('assignedLabourers/' + auth.uid).exists()",
        ".write": "data.child('farmerId').val() === auth.uid"
      }
    },
    "pettyCash": {
      ".indexOn": ["farmerId", "transaction/date"],
      "$pettyCashId": {
        ".read": "data.child('farmerId').val() === auth.uid",
        ".write": "data.child('farmerId').val() === auth.uid"
      }
    },
    "businessPartners": {
      ".indexOn": ["farmerId"],
      "$partnerId": {
        ".read": "$partnerId === auth.uid || data.child('farmerId').val() === auth.uid",
        ".write": "data.child('farmerId').val() === auth.uid || $partnerId === auth.uid"
      }
    }
  }
}
```

---

## Data Validation Rules

### Phone Number
- Format: 10 digits
- Unique across users
- Required for all users

### Bank Account
- Encrypted before storage
- Optional field
- Validated format: IFSC code, account number

### Dates
- All timestamps in milliseconds
- UTC timezone
- Must be valid date range

### Amounts
- Non-negative numbers
- Maximum 2 decimal places
- Currency: INR (₹)

### Enums
Strictly validated against predefined values:
- UserRole: Farmer, Labourer, Business Partner
- PaymentMode: Cash, Online, UPI, Cheque
- PaymentStatus: Paid, Pending, Partial
- CropStatus: Planning, Sowing, Growing, Harvesting, Completed
- ShiftType: Full Day, Half Day, Hourly

---

## Migration Strategy

### From v1.0 to v2.0

1. **Users Collection**
   - Add new fields: address, bankDetails, preferences
   - Migrate existing phone-based auth to enhanced profile
   - Default role: Farmer (for existing users)

2. **Create New Collections**
   - Farms, Crops, Storage, Schedules, PettyCash, BusinessPartners
   - Initialize with empty arrays/default values

3. **Enhance Existing Collections**
   - Expenses: Add payment details, vendor info
   - Labour: Add workDetails, performance metrics
   - Create Sales from existing data if applicable

4. **Data Integrity**
   - Verify all foreign key relationships
   - Validate enums and required fields
   - Run data quality checks

5. **Rollback Plan**
   - Backup existing database before migration
   - Keep v1.0 schema accessible for 30 days
   - Provide data export for users

---

## Performance Optimization

### Indexing Strategy
- Index all foreign keys
- Index frequently queried date fields
- Index status/state fields used in filtering

### Query Optimization
- Use `.equalTo()` for exact matches
- Limit results with `.limitToFirst()` or `.limitToLast()`
- Use `.orderByChild()` only on indexed fields

### Caching
- Cache frequently accessed reference data
- Use local SQLite for offline access
- Implement smart sync strategy

---

## Backup & Recovery

### Automated Backups
- Daily: Full database backup
- Hourly: Incremental backups
- Retention: 30 days

### Recovery Procedures
1. Identify backup timestamp
2. Restore from Firebase backup
3. Verify data integrity
4. Notify affected users
5. Document incident

---

**Last Updated**: October 31, 2025
**Version**: 2.0
**Maintained By**: GreenLedger Development Team
