package com.app.bank.dto;

import java.util.Date;

public record NoticeResponse(
        long noticeId,
        String noticeSummary,
        String noticeDetails,
        Date noticeBeginDate,
        Date noticeEndDate
) {}