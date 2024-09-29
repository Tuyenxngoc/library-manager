package com.example.librarymanager.domain.dto.response;

import com.example.librarymanager.constant.CommonConstant;
import com.example.librarymanager.domain.entity.Log;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetLogResponseDto {

    private final String feature; // Chức năng được thao tác

    private final String event; // Sự kiện (Truy cập, Thêm, Xóa, v.v.)

    private final String content; // Nội dung mô tả chi tiết

    private final String user; // Người thực hiện

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonConstant.PATTERN_DATE_TIME)
    private final LocalDateTime timestamp; // Thời gian hành động

    public GetLogResponseDto(Log log) {
        this.feature = log.getFeature();
        this.event = log.getEvent();
        this.content = log.getContent();
        this.timestamp = log.getTimestamp();
        this.user = log.getUser().getUsername();
    }
}