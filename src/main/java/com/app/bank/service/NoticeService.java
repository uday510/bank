package com.app.bank.service;

import com.app.bank.dto.NoticeResponse;
import com.app.bank.model.Notice;
import com.app.bank.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public List<Notice> getNotices() {
        return noticeRepository.findAllActiveNotices();
    }

    public List<NoticeResponse> getAllActiveNotices() {
        return Optional.ofNullable(noticeRepository.findAllActiveNotices())
                .orElse(List.of())
                .stream()
                .map(notice -> new NoticeResponse(
                        notice.getNoticeId(),
                        notice.getNoticeSummary(),
                        notice.getNoticeDetails(),
                        notice.getNoticeBeginDate(),
                        notice.getNoticeEndDate()
                ))
                .toList();
    }
}
