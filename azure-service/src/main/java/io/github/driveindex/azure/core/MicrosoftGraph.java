package io.github.driveindex.azure.core;

import kotlin.text.Charsets;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author sgpublic
 * @Date 2022/8/8 12:03
 */
@Slf4j
@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MicrosoftGraph {
    private final String clientId;
    private final String clientSecret;

    public static final String scope = URLEncoder
            .encode("offline_access files.read.all", Charsets.UTF_8);

    private static final Map<String, MicrosoftGraph> cache = new HashMap<>();

    @NonNull
    public static MicrosoftGraph of(
            String clientId, String clientSecret
    ) {
        if (!cache.containsKey(clientId)) {
            MicrosoftGraph microsoftGraph = new MicrosoftGraph(clientId, clientSecret);
            cache.put(clientId, microsoftGraph);
        }
        return cache.get(clientId);
    }

    @Nullable
    public static MicrosoftGraph of(String clientId) {
        return cache.get(clientId);
    }

    private record Client(String clientId, String clientSecret) implements Serializable {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Client client)) return false;
            return Objects.equals(clientId, client.clientId) && Objects.equals(clientSecret, client.clientSecret);
        }

        @Override
        public int hashCode() {
            return Objects.hash(clientId, clientSecret);
        }
    }
}
