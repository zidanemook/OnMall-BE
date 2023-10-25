package shop.mookmall.com.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mookmall.com.core.auth.jwt.MyJwtProvider;
import shop.mookmall.com.core.exception.Exception400;
import shop.mookmall.com.core.exception.Exception401;
import shop.mookmall.com.model.token.RefreshTokenEntity;
import shop.mookmall.com.model.token.TokenRepository;
import shop.mookmall.com.model.token.TokenStatus;
import shop.mookmall.com.model.user.User;
import shop.mookmall.com.model.user.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static shop.mookmall.com.core.auth.jwt.MyJwtProvider.REFRESH_SECRET;

@Slf4j
@RequiredArgsConstructor
@Service
public class RefreshService {

    private final TokenRepository tokenRepository;

    private final UserRepository userRepository;

    @Transactional
    public String renewAccessToken(HttpServletRequest request) {

        String prefixJwt = request.getHeader(MyJwtProvider.HEADER_REFRESH);
        String refreshjwt = prefixJwt.replace(MyJwtProvider.TOKEN_PREFIX, "");

        DecodedJWT decodedJWT = null;

        try {
            decodedJWT = JWT.require(Algorithm.HMAC512(REFRESH_SECRET)).build().verify(refreshjwt);
        } catch (SignatureVerificationException sve) {
            log.error("리프레시 토큰 검증 실패");
            throw new Exception401("리프레시 토큰 검증 실패");
        } catch (TokenExpiredException tee) {
            log.error("리프레시 토큰 만료됨");
            throw new Exception401("리프레시 토큰 만료");
        }

        String uuid = decodedJWT.getClaim("uuid").asString();
        // UUID로 리프레시 토큰 조회 및 검증
        RefreshTokenEntity refreshTokenEntity = tokenRepository.findByUuidAndStatus(uuid, TokenStatus.VALID)
                .orElseThrow(() -> new Exception401("유효한 리프레시 토큰이 존재하지 않습니다."));

        User userPS = userRepository.findById(refreshTokenEntity.getUser().getId())
                .orElseThrow(() -> new Exception401("해당하는 사용자가 없습니다."));

        // 액세스 토큰 재발급
        String accessjwt = MyJwtProvider.createAccess(userPS);

        return accessjwt;
    }

    @Transactional
    public boolean revokeRefreshToken(HttpServletRequest request) {

        String prefixJwt = request.getHeader(MyJwtProvider.HEADER_REFRESH);
        String refreshjwt = prefixJwt.replace(MyJwtProvider.TOKEN_PREFIX, "");

        DecodedJWT decodedJWT = null;

        try {
            decodedJWT = JWT.require(Algorithm.HMAC512(REFRESH_SECRET)).build().verify(refreshjwt);
        } catch (SignatureVerificationException sve) {
            log.error("리프레시 토큰 검증 실패");
        } catch (TokenExpiredException tee) {
            log.error("리프레시 토큰 만료됨");

            String uuid = decodedJWT.getClaim("uuid").asString();
            RefreshTokenEntity RefreshTokenPS = tokenRepository.findByUuid(uuid)
                    .orElseThrow(()->{
                        log.error("리프레시 토큰 존재하지 않음");
                        return new Exception400("refreshtoken", "리프레시 토큰 존재하지 않음");
                    });
            RefreshTokenPS.setStatus(TokenStatus.EXPIRED);

            return false;

        }

        String uuid = decodedJWT.getClaim("uuid").asString();
        // UUID로 리프레시 토큰 조회 및 검증
        RefreshTokenEntity RefreshTokenPS = tokenRepository.findByUuidAndStatus(uuid, TokenStatus.VALID)
                .orElseThrow(() -> {
                    log.error("유효한 리프레시 토큰 존재하지 않음");
                    return new Exception400("refreshtoken", "유효한 리프레시 토큰이 존재하지 않습니다.");
                });

        RefreshTokenPS.setStatus(TokenStatus.REVOKED);

        return true;
    }

    @Transactional
    public void deleteExpiredRefreshToken()
    {
        List<RefreshTokenEntity> expiredTokens = tokenRepository.findByStatus(TokenStatus.EXPIRED);
        List<RefreshTokenEntity> revokedTokens = tokenRepository.findByStatus(TokenStatus.REVOKED);

        tokenRepository.deleteAllInBatch(expiredTokens);
        tokenRepository.deleteAllInBatch(revokedTokens);
    }
}

