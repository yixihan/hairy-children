package com.wq.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author : yixihan
 * @create : 2022-02-07-13:27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class PhotoProperties {

    @Value ("${upload.paths}")
    private String paths;

    @Value ("${upload.adoptPaths}")
    private String adoptPaths;

    @Value ("${upload.avatarPaths}")
    private String avatarPaths;

    @Value ("${upload.cluePaths}")
    private String cluePaths;

    @Value ("${upload.titlePaths}")
    private String titlePaths;

}
