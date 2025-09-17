package team.incube.gwangjutalentfestivalserver.domain.vote.usecase;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.incube.gwangjutalentfestivalserver.domain.seat.dto.response.GetSeatResponse;
import team.incube.gwangjutalentfestivalserver.domain.seat.entity.SeatReservation;
import team.incube.gwangjutalentfestivalserver.domain.user.entity.User;
import team.incube.gwangjutalentfestivalserver.domain.vote.dto.response.RandomSeatVoteResult;
import team.incube.gwangjutalentfestivalserver.global.exception.HttpException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RandomSeatVoteUsecase {

    private final RandomReservationExtractor extractor;

    @Transactional
    public RandomSeatVoteResult execute(Long teamId, int count) {
        List<SeatReservation> reservations = extractor.extractRandomReservations(teamId, count);

        if (reservations.isEmpty()) {
            throw new HttpException(HttpStatus.NOT_FOUND, "해당 팀에 예약된 사용자가 없습니다.");
        }

        List<GetSeatResponse> seatResponses = reservations.stream()
                .map(r -> new GetSeatResponse(
                        String.valueOf(r.getSeatSection()),
                        r.getSeatNumber()
                ))
                .collect(Collectors.toList());

        byte[] excelBytes;
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Random Voters");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("번호");
            header.createCell(1).setCellValue("전화번호");

            List<User> selectedUsers = reservations.stream()
                    .map(SeatReservation::getUser)
                    .collect(Collectors.toList());

            int rowIdx = 1;
            for (int i = 0; i < selectedUsers.size(); i++) {
                User user = selectedUsers.get(i);
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(i + 1);
                row.createCell(1).setCellValue(user.getPhoneNumber());
            }

            workbook.write(bos);
            excelBytes = bos.toByteArray();
        } catch (IOException e) {
            throw new HttpException(HttpStatus.INTERNAL_SERVER_ERROR, "엑셀 파일 생성 실패");
        }

        return new RandomSeatVoteResult(seatResponses, excelBytes);
    }
}

