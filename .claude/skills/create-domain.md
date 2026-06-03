---
name: create-domain
description: 새로운 도메인 전체 구조를 스캐폴딩하는 스킬. 새 비즈니스 도메인이 필요할 때 사용한다.
---

# /create-domain 스킬

새로운 도메인 전체 구조(패키지, 기본 클래스)를 생성한다.

## 사용법

```
/create-domain {도메인명}

예시:
/create-domain notification
/create-domain schedule
```

## 생성할 파일 구조

```
src/main/java/team/incube/gwangjutalentfestivalserver/domain/{도메인}/
├── controller/
│   └── {Domain}Controller.java
├── usecase/
│   └── (비어있음 — 구체적 UseCase는 /implement-usecase로)
├── entity/
│   └── {Domain}.java
├── repository/
│   └── {Domain}Repository.java
├── dto/
│   ├── request/
│   └── response/
└── enums/
    └── (필요시)
```

## 생성 스크립트

### Controller 템플릿
```java
package team.incube.gwangjutalentfestivalserver.domain.{도메인}.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/{도메인s}")
public class {Domain}Controller {
    // UseCase들이 추가될 예정
}
```

### Entity 템플릿
```java
package team.incube.gwangjutalentfestivalserver.domain.{도메인}.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "{도메인}")
public class {Domain} {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // 필드 추가 예정
    
    @Builder
    public {Domain}(/* 파라미터 */) {
        // 초기화
    }
}
```

### Repository 템플릿
```java
package team.incube.gwangjutalentfestivalserver.domain.{도메인}.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.incube.gwangjutalentfestivalserver.domain.{도메인}.entity.{Domain};

public interface {Domain}Repository extends JpaRepository<{Domain}, Long> {
    // 쿼리 메서드 추가 예정
}
```

## 생성 후

1. `./gradlew compileJava` 확인
2. SSE가 필요한 도메인이면 `global/sse/`에 EmitterManager 추가
3. `global/security/SecurityConfig.java`에 경로 패턴 추가
4. `/implement-usecase`로 구체적 UseCase 구현