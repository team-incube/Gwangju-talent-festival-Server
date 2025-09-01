package team.incube.gwangjutalentfestivalserver.domain.vote.usecase;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gwangjutalentfestivalserver.domain.seat.entity.SeatReservation;
import team.incube.gwangjutalentfestivalserver.domain.seat.repository.SeatReservationRepository;
import team.incube.gwangjutalentfestivalserver.domain.user.entity.User;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteRandomExtractUsecase {

    private final SeatReservationRepository seatReservationRepository;

    @Transactional(readOnly = true)
    public byte[] execute(Long teamId, int count) {
        List<User> reservedUsers = seatReservationRepository.findAllByTeamId(teamId)
                .stream()
                .map(SeatReservation::getUser)
                .collect(Collectors.toList());

        if (reservedUsers.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, "해당 팀을 찾을 수 없습니다.");
        }

        Collections.shuffle(reservedUsers);

        List<User> selected = reservedUsers.stream()
                .limit(count)
                .collect(Collectors.toList());

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Random Voters");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("번호");
            header.createCell(1).setCellValue("전화번호");

            int rowIdx = 1;
            for (int i = 0; i < selected.size(); i++) {
                User user = selected.get(i);
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(i + 1);
                row.createCell(1).setCellValue(user.getPhoneNumber());
            }

            workbook.write(bos);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR, "엑셀 파일 생성 실패");
        }
    }
}
