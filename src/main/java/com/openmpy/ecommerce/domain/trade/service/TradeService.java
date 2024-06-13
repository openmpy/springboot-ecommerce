package com.openmpy.ecommerce.domain.trade.service;

import com.openmpy.ecommerce.domain.coin.entity.CoinEntity;
import com.openmpy.ecommerce.domain.coin.repository.CoinRepository;
import com.openmpy.ecommerce.domain.member.entity.MemberEntity;
import com.openmpy.ecommerce.domain.member.repository.MemberRepository;
import com.openmpy.ecommerce.domain.trade.dto.request.BuyTradeRequestDto;
import com.openmpy.ecommerce.domain.trade.dto.request.SellTradeRequestDto;
import com.openmpy.ecommerce.domain.trade.dto.response.BuyTradeResponseDto;
import com.openmpy.ecommerce.domain.trade.dto.response.SellTradeResponseDto;
import com.openmpy.ecommerce.domain.trade.entity.TradeEntity;
import com.openmpy.ecommerce.domain.trade.repository.TradeRepository;
import com.openmpy.ecommerce.domain.wallet.entity.WalletEntity;
import com.openmpy.ecommerce.domain.wallet.repository.WalletRepository;
import com.openmpy.ecommerce.global.exception.CustomException;
import com.openmpy.ecommerce.global.exception.constants.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Transactional
@Service
public class TradeService {

    private final TradeRepository tradeRepository;
    private final MemberRepository memberRepository;
    private final CoinRepository coinRepository;
    private final WalletRepository walletRepository;

    public BuyTradeResponseDto buy(BuyTradeRequestDto requestDto, String email) {
        MemberEntity memberEntity = validateMemberEntity(email);
        CoinEntity coinEntity = validateCoinEntity(requestDto.coinId());

        BigDecimal totalPrice = requestDto.amount().multiply(requestDto.price());
        if (memberEntity.getBalance().compareTo(totalPrice) < 0) {
            throw new CustomException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        TradeEntity tradeEntity = tradeRepository.save(requestDto.buy(memberEntity, coinEntity));
        memberEntity.minusBalance(totalPrice);
        return new BuyTradeResponseDto(tradeEntity);
    }

    public SellTradeResponseDto sell(SellTradeRequestDto requestDto, String email) {
        MemberEntity memberEntity = validateMemberEntity(email);
        CoinEntity coinEntity = validateCoinEntity(requestDto.coinId());

        WalletEntity walletEntity = walletRepository.findByMemberEntityAndCoinEntity(memberEntity, coinEntity)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_WALLET));

        if (walletEntity.getAmount().compareTo(requestDto.amount()) < 0) {
            throw new CustomException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        TradeEntity tradeEntity = tradeRepository.save(requestDto.sell(memberEntity, coinEntity));
        walletEntity.minusAmount(requestDto.amount());
        return new SellTradeResponseDto(tradeEntity);
    }

    private MemberEntity validateMemberEntity(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    private CoinEntity validateCoinEntity(Long coinId) {
        return coinRepository.findById(coinId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_COIN));
    }
}
