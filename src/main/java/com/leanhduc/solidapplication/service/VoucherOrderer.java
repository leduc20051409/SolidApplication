package com.leanhduc.solidapplication.service;

import com.leanhduc.solidapplication.model.Voucher;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class VoucherOrderer {

    private VoucherOrderer() {}

    public static List<Voucher> followRequestOrder(
            List<String> requestedCodes, List<Voucher> fetchedVouchers) {
        Map<String, Voucher> vouchersByCode = new HashMap<>();
        for (Voucher voucher : fetchedVouchers) {
            vouchersByCode.put(normalize(voucher.getCode()), voucher);
        }

        List<Voucher> orderedVouchers = new ArrayList<>();
        Set<String> addedCodes = new HashSet<>();
        for (String requestedCode : requestedCodes) {
            if (requestedCode == null) {
                continue;
            }

            String normalizedCode = normalize(requestedCode);
            Voucher voucher = vouchersByCode.get(normalizedCode);
            if (voucher != null && addedCodes.add(normalizedCode)) {
                orderedVouchers.add(voucher);
            }
        }
        return orderedVouchers;
    }

    private static String normalize(String code) {
        return code.toUpperCase(Locale.ROOT);
    }
}
