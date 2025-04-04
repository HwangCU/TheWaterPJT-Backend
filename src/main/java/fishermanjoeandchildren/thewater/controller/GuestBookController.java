package fishermanjoeandchildren.thewater.controller;

import fishermanjoeandchildren.thewater.data.dto.ApiResponse;
import fishermanjoeandchildren.thewater.data.dto.GuestBookRequestDto;
import fishermanjoeandchildren.thewater.security.JwtUtil;
import fishermanjoeandchildren.thewater.service.GuestBookService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/guest-book")
public class GuestBookController {

    private final GuestBookService guestBookService;
    private final JwtUtil jwtUtil;

    @GetMapping("/read/{aquarium-id}")
    public ApiResponse<?> getGuestBookComments(@RequestParam("aquarium-id") Long aquariumId, HttpServletRequest request){
        String token = jwtUtil.resolveToken(request);
        Long memberId = jwtUtil.extractUserId(token);

        ApiResponse<?> result = guestBookService.getComments(aquariumId,memberId);

        return result;
    }

    @PostMapping("/write/{aquarium-id}")
    public ApiResponse<?> writeGuestBookComment(@RequestBody GuestBookRequestDto guestBookDto, @RequestParam("aquarium-id") Long aquariumId, HttpServletRequest request){
        String token = jwtUtil.resolveToken(request);
        Long memberId = jwtUtil.extractUserId(token);

        ApiResponse<?> result = guestBookService.writeComment(guestBookDto,aquariumId, memberId);

        return result;
    }
}
