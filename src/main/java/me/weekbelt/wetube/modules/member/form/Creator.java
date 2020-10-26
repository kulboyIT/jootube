package me.weekbelt.wetube.modules.member.form;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Creator {

    private Long id;
    private String name;
    private String email;

    @Builder
    public Creator(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
