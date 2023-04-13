package com.hyukolog.api.request;

import com.hyukolog.api.domain.Post;
import com.hyukolog.api.exception.InvalidRequest;
import lombok.*;

import javax.validation.constraints.NotBlank;

@ToString
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void validate() {
        if(title.contains("바보")) {
            throw new InvalidRequest("title", "제목에 바보를 포함할 수 없습니다.");
        }
    }

    // 빌더의 장점
    // - 생성자의 파라미터가 많아지면, 생성자를 사용하는 것보다 빌더를 사용하는 것이 좋다.
    // - 빌더를 사용하면, 생성자의 파라미터 순서를 신경쓰지 않아도 된다.
    // - 빌더를 사용하면, 생성자의 파라미터가 많아지더라도, 생성자를 사용하는 것보다 가독성이 좋다.
    // - 빌더를 사용하면 필요한 값만 받을 수 있다. -> 오버로딩 가능한 조건 찾아보기
    // - 객체의 불변ㅜ
}
