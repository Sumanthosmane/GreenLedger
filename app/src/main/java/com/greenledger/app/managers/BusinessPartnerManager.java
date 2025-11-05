package com.greenledger.app.managers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.greenledger.app.firebase.FirebaseHelper;
import com.greenledger.app.models.BusinessPartner;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BusinessPartnerManager {
    private final DatabaseReference partnersRef;

    public BusinessPartnerManager() {
        this.partnersRef = FirebaseHelper.getInstance().getBusinessPartnersRef();
    }

    public Task<Void> createBusinessPartner(BusinessPartner partner) {
        String partnerId = partnersRef.push().getKey();
        if (partnerId != null) {
            partner.setPartnerId(partnerId);
            return partnersRef.child(partnerId).setValue(partner);
        }
        return null;
    }

    public Task<Void> updateBusinessPartner(BusinessPartner partner) {
        if (partner.getPartnerId() != null) {
            return partnersRef.child(partner.getPartnerId()).setValue(partner);
        }
        return null;
    }

    public Task<Void> deleteBusinessPartner(String partnerId) {
        return partnersRef.child(partnerId).removeValue();
    }

    public void getPartnerById(String partnerId, ValueEventListener listener) {
        partnersRef.child(partnerId).addValueEventListener(listener);
    }

    public void getFarmerPartners(String farmerId, ValueEventListener listener) {
        Query query = partnersRef.orderByChild("farmerId").equalTo(farmerId);
        query.addValueEventListener(listener);
    }

    public Task<Void> updatePartnerRating(String partnerId, float rating) {
        return partnersRef.child(partnerId)
            .child("metadata")
            .child("rating")
            .setValue(rating);
    }

    public Task<Void> updateTransactionHistory(String partnerId, double amount, String type) {
        return partnersRef.child(partnerId)
            .child("transactionHistory")
            .get()
            .continueWithTask(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    BusinessPartner.TransactionHistory history =
                        task.getResult().getValue(BusinessPartner.TransactionHistory.class);

                    if (history == null) {
                        history = new BusinessPartner.TransactionHistory();
                    }

                    history.setTotalPurchases(history.getTotalPurchases() + 1);
                    history.setTotalAmount(history.getTotalAmount() + amount);
                    history.setLastPurchaseDate(System.currentTimeMillis());

                    // Update outstanding amount based on transaction type
                    if ("credit".equalsIgnoreCase(type)) {
                        history.setOutstandingAmount(history.getOutstandingAmount() + amount);
                    } else if ("payment".equalsIgnoreCase(type)) {
                        history.setOutstandingAmount(history.getOutstandingAmount() - amount);
                    }

                    return partnersRef.child(partnerId)
                        .child("transactionHistory")
                        .setValue(history);
                }
                return null;
            });
    }

    public void getPartnersByType(String farmerId, String type, ValueEventListener listener) {
        Query query = partnersRef.orderByChild("farmerId").equalTo(farmerId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<BusinessPartner> filteredPartners = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BusinessPartner partner = snapshot.getValue(BusinessPartner.class);
                    if (partner != null && partner.getPartnerInfo() != null &&
                        type.equals(partner.getPartnerInfo().getType())) {
                        filteredPartners.add(partner);
                    }
                }
                if (listener != null) {
                    listener.onDataChange(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (listener != null) {
                    listener.onCancelled(databaseError);
                }
            }
        });
    }

    public void searchPartners(String farmerId, String query, ValueEventListener listener) {
        Query baseQuery = partnersRef.orderByChild("farmerId").equalTo(farmerId);
        baseQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<BusinessPartner> filteredPartners = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BusinessPartner partner = snapshot.getValue(BusinessPartner.class);
                    if (partner != null && partner.getPartnerInfo() != null &&
                        partner.getPartnerInfo().getBusinessName() != null &&
                        partner.getPartnerInfo().getBusinessName().toLowerCase().contains(query.toLowerCase())) {
                        filteredPartners.add(partner);
                    }
                }
                if (listener != null) {
                    listener.onDataChange(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (listener != null) {
                    listener.onCancelled(databaseError);
                }
            }
        });
    }
}
