package io.github.driveindex.azure.feign;

import io.github.driveindex.common.dto.azure.drive.AccountDto;
import io.github.driveindex.common.dto.result.SuccessResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.LinkedList;

/**
 * @author sgpublic
 * @Date 2022/8/9 11:58
 */
@FeignClient("admin-service")
public interface AdminAccountClient {
    @GetMapping("/admin/api/azure_client/{aClient}")
    SuccessResult<LinkedList<AccountDto>> getAccountByClient(@PathVariable String aClient);
}
