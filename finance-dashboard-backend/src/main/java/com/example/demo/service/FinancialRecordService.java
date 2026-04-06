package com.example.demo.service;

import com.example.demo.dto.RecordRequest;
import com.example.demo.entity.FinancialRecord;
import com.example.demo.entity.RecordType;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.FinancialRecordRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class FinancialRecordService {

    @Autowired
    private FinancialRecordRepository recordRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public FinancialRecord createRecord(RecordRequest request, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        FinancialRecord record = FinancialRecord.builder()
                .amount(request.getAmount())
                .type(request.getType())
                .category(request.getCategory())
                .date(request.getDate())
                .description(request.getDescription())
                .user(user)
                .build();

        return recordRepository.save(record);
    }

    public Page<FinancialRecord> getRecords(String username, Role role, RecordType type, String category, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Specification<FinancialRecord> spec = Specification.where(null);

        if (role == Role.VIEWER) {
             User user = userRepository.findByUsername(username).orElseThrow();
             spec = spec.and((root, query, cb) -> cb.equal(root.get("user"), user));
        }

        if (type != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("type"), type));
        }

        if (category != null && !category.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("category"), category));
        }

        if (startDate != null && endDate != null) {
            spec = spec.and((root, query, cb) -> cb.between(root.get("date"), startDate, endDate));
        }

        return recordRepository.findAll(spec, pageable);
    }

    @Transactional
    public FinancialRecord updateRecord(Long id, RecordRequest request) {
        FinancialRecord record = recordRepository.findById(id).orElseThrow(() -> new RuntimeException("Record not found"));

        record.setAmount(request.getAmount());
        record.setType(request.getType());
        record.setCategory(request.getCategory());
        record.setDate(request.getDate());
        record.setDescription(request.getDescription());

        return recordRepository.save(record);
    }

    @Transactional
    public void deleteRecord(Long id) {
        recordRepository.deleteById(id);
    }
}
