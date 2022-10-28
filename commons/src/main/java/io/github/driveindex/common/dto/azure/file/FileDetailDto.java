package io.github.driveindex.common.dto.azure.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author sgpublic
 * @Date 2022/8/2 20:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FileDetailDto extends AzureItemDto.Detail implements Cloneable {
    private String quickXorHash;
    private String sha1Hash;
    private String sha256Hash;

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public FileDetailDto clone() {
        FileDetailDto newObj = new FileDetailDto();
        newObj.setQuickXorHash(this.getQuickXorHash());
        newObj.setSha1Hash(this.getSha1Hash());
        newObj.setSha256Hash(this.getSha256Hash());
        return newObj;
    }
}
