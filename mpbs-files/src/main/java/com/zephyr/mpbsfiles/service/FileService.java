package com.zephyr.mpbsfiles.service;

import com.zephyr.mpbscommon.utils.Result;
import com.zephyr.mpbsfiles.dto.FilesProcessDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    /**
     * 根据用户ID获取该用户角色可访问的文件列表
     * @param userId 用户ID
     * @return 文件处理DTO列表
     */
    List<FilesProcessDTO> getFilesListByRole(String userId);

    /**
     * 上传文件
     * @param file 上传的文件
     * @param uploaderId 上传者ID
     * @return 上传后文件的DTO对象
     * @throws IOException 文件操作异常
     */
    FilesProcessDTO uploadFile(MultipartFile file, String uploaderId) throws IOException;

    /**
     * 根据文件ID查询文件信息
     * @param id 文件ID
     * @return 文件处理DTO
     */
    FilesProcessDTO getFileById(String id);

    /**
     * 分享指定文件
     * @param fileId 文件ID
     * @return 操作结果
     */
    Result shareFile(String fileId);

    /**
     * 删除指定文件
     * @param fileId 文件ID
     * @return 操作结果
     */
    Result deleteFile(String fileId);

}
