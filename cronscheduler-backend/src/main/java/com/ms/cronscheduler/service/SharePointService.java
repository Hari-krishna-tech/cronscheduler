package com.ms.cronscheduler.service;

import com.microsoft.graph.models.DriveItem;
import com.microsoft.graph.models.DriveItemCreateLinkParameterSet;
import com.microsoft.graph.models.DriveRecipient;
import com.microsoft.graph.models.Permission;
import com.microsoft.graph.requests.GraphServiceClient;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@Service
public class SharePointService {

    @Value("${sharepoint.site-id}")
    private String siteId;

    @Value("${sharepoint.drive-id}")
    private String driveId;

    private final GraphServiceClient<Request> graphClient;

    public SharePointService(GraphServiceClient<Request> graphClient) {
        this.graphClient = graphClient;
    }

    public String uploadFileAndGetViewOnlyUrl(MultipartFile file) throws IOException {
        // Upload file
        InputStream fileStream = file.getInputStream();
        DriveItem uploadedFile = graphClient.sites(siteId).drives(driveId).root()
                .itemWithPath(file.getOriginalFilename())
                .content()
                .buildRequest()
                .put(fileStream.readAllBytes());

        // Get sharing link
        assert uploadedFile != null;

        DriveItemCreateLinkParameterSet parameterSet = DriveItemCreateLinkParameterSet
                .newBuilder()
                .withType("view") // or "edit", "embed"
                .withScope("anonymous") // or "organization"
                .build();
        Permission sharedDriveItem = graphClient.sites(siteId).drives(driveId).items(uploadedFile.id)
                .createLink(parameterSet)
                .buildRequest()
                .post();

        return sharedDriveItem.link.webUrl;
    }
}
