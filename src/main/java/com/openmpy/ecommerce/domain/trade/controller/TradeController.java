package com.openmpy.ecommerce.domain.trade.controller;

import com.openmpy.ecommerce.domain.trade.controller.docs.TradeControllerDocs;
import com.openmpy.ecommerce.domain.trade.dto.request.BuyTradeRequestDto;
import com.openmpy.ecommerce.domain.trade.dto.request.SellTradeRequestDto;
import com.openmpy.ecommerce.domain.trade.dto.response.BuyTradeResponseDto;
import com.openmpy.ecommerce.domain.trade.dto.response.GetTradeResponseDto;
import com.openmpy.ecommerce.domain.trade.dto.response.SellTradeResponseDto;
import com.openmpy.ecommerce.domain.trade.service.TradeService;
import com.openmpy.ecommerce.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/trades")
@RestController
public class TradeController implements TradeControllerDocs {

    private final TradeService tradeService;

    @PostMapping("/buy")
    public ResponseEntity<BuyTradeResponseDto> buy(
            @Valid @RequestBody BuyTradeRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String email = userDetails.getUsername();
        BuyTradeResponseDto responseDto = tradeService.buy(email, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/sell")
    public ResponseEntity<SellTradeResponseDto> sell(
            @Valid @RequestBody SellTradeRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String email = userDetails.getUsername();
        SellTradeResponseDto responseDto = tradeService.sell(email, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @DeleteMapping("/{tradeId}")
    public ResponseEntity<Void> cancel(
            @PathVariable Long tradeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String email = userDetails.getUsername();
        tradeService.cancel(email, tradeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<GetTradeResponseDto>> gets(
            @RequestParam(defaultValue = "all") String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<GetTradeResponseDto> responseDto = tradeService.gets(type, page, size);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/{tradeId}")
    public ResponseEntity<Void> transaction(
            @PathVariable Long tradeId,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        String email = userDetails.getUsername();
        tradeService.transaction(email, tradeId);
        return ResponseEntity.noContent().build();
    }
}
