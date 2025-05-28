package com.guesthouse.wishboard.service;

import com.guesthouse.wishboard.dto.HallOfFameDto;
import com.guesthouse.wishboard.repository.HallOfFameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HallOfFameService {

    private final HallOfFameRepository hallOfFameRepository;

    //명예의 전당 Top 10 조회
    public List<HallOfFameDto> getHallOfFame() {
        return hallOfFameRepository.findTop10Trophies(PageRequest.of(0, 10));
    }
}
