package com.finance.dashboard.service;

import com.finance.dashboard.model.FinancialRecord;
import com.finance.dashboard.repository.FinancialRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class FinancialRecordService {

    @Autowired
    private FinancialRecordRepository recordRepo;

    public FinancialRecord addRecord(FinancialRecord record) {
        return recordRepo.save(record);              // save record to DB
    }

    public List<FinancialRecord> getAllRecords() {
        return recordRepo.findAll();                // get all records
    }

    public FinancialRecord updateRecord(Long id, FinancialRecord record) {
        FinancialRecord existing = recordRepo.findById(id).orElseThrow(() -> new RuntimeException("Record not found"));
        // update fields
        existing.setAmount(record.getAmount());
        existing.setType(record.getType());
        existing.setCategory(record.getCategory());
        existing.setDescription(record.getDescription());
        existing.setDate(record.getDate());
        return recordRepo.save(existing);
    }

    public void deleteRecord(Long id) {
        if (!recordRepo.existsById(id)) throw new RuntimeException("Record not found");
        recordRepo.deleteById(id);
    }

    public List<FinancialRecord> filterRecords(String type, String category, LocalDate start, LocalDate end) {
        List<FinancialRecord> all = recordRepo.findAll();

        if(type != null) {
            all = all.stream().filter(r -> r.getType().equalsIgnoreCase(type)).toList();
        } else if(category != null) {
            all = all.stream().filter(r -> r.getCategory().equalsIgnoreCase(category)).toList();
        } else if(start != null && end != null) {
            all = all.stream().filter(r -> !r.getDate().isBefore(start) && !r.getDate().isAfter(end)).toList();
        }

        return all;
    }

    public Summary getSummary() {
        List<FinancialRecord> all = recordRepo.findAll();

        double income = 0;
        double expense = 0;

        Map<String, Double> categoryTotals = new java.util.HashMap<>();
        List<FinancialRecord> recentRecords = new java.util.ArrayList<>();
        Map<String, Double> monthlyTrends = new java.util.HashMap<>();

        for(FinancialRecord r : all) {
            if("INCOME".equalsIgnoreCase(r.getType())) income += r.getAmount();
            else if("EXPENSE".equalsIgnoreCase(r.getType())) expense += r.getAmount();

            // category totals
            categoryTotals.put(r.getCategory(), categoryTotals.getOrDefault(r.getCategory(),0.0) + r.getAmount());

            // monthly trends
            String monthKey = r.getDate().getYear() + "-" + r.getDate().getMonthValue();
            monthlyTrends.put(monthKey, monthlyTrends.getOrDefault(monthKey,0.0) + r.getAmount());
        }

        // recent 5 records
        all.sort((r1,r2) -> r2.getDate().compareTo(r1.getDate()));
        for(int i=0; i<Math.min(5, all.size()); i++) {
            recentRecords.add(all.get(i));
        }

        double net = income - expense;

        return new Summary(income, expense, net, categoryTotals, recentRecords, monthlyTrends);
    }

    public static class Summary {
        public double totalIncome;
        public double totalExpense;
        public double netBalance;
        public Map<String, Double> categoryTotals;      // category-wise
        public List<FinancialRecord> recentRecords;    // recent activity
        public Map<String, Double> monthlyTrends;      // monthly/weekly trends

        public Summary(double totalIncome, double totalExpense, double netBalance,
                       Map<String, Double> categoryTotals,
                       List<FinancialRecord> recentRecords,
                       Map<String, Double> monthlyTrends){
            this.totalIncome = totalIncome;
            this.totalExpense = totalExpense;
            this.netBalance = netBalance;
            this.categoryTotals = categoryTotals;
            this.recentRecords = recentRecords;
            this.monthlyTrends = monthlyTrends;
        }

    }
}