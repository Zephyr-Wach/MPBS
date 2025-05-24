package com.zephyr.mpbsfiles.service;

import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsfiles.dto.FilesProcessDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    List<FilesProcessDTO> getFilesListByRole(String userId);
    // 上传文件，返回上传后文件的 DTO
    FilesProcessDTO uploadFile(MultipartFile file, String uploaderId) throws IOException;

    // 根据id查询文件信息
    FilesProcessDTO getFileById(String id);

    Result shareFile(String fileId);

    Result deleteFile(String fileId);
}
