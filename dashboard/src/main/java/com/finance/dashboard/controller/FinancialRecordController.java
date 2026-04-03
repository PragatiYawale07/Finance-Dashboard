package com.finance.dashboard.controller;

import com.finance.dashboard.model.FinancialRecord;
import com.finance.dashboard.service.FinancialRecordService;
import com.finance.dashboard.service.FinancialRecordService.Summary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/records")
public class FinancialRecordController {

    @Autowired
    private FinancialRecordService recordService;

    @PostMapping
    public FinancialRecord addRecord(@RequestBody FinancialRecord record) {
        return recordService.addRecord(record);
    }

    @GetMapping
    public List<FinancialRecord> getAllRecords() {
        return recordService.getAllRecords();
    }

    @PutMapping("/{id}")
    public FinancialRecord updateRecord(@PathVariable Long id, @RequestBody FinancialRecord record) {
        return recordService.updateRecord(id, record);
    }

    @DeleteMapping("/{id}")
    public String deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
        return "Record deleted successfully";
    }

    @GetMapping("/filter")
    public List<FinancialRecord> filterRecords(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return recordService.filterRecords(type, category, start, end);
    }

    @GetMapping("/summary")
    public Summary getSummary() {
        return recordService.getSummary();
    }
}