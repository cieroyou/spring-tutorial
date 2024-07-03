# MultipartFile을 사용하는 이유

- **메모리 효율성**
    - 멀티파트 요청은 서버가 파일을 메모리에 로드하지 않고 직접 디스크로 스트리밍하여 메모리 사용을 최소화 할 수 있다. 특히 대용량 파일을 업로드 하는 경우 메모리 사용을 최소화 할 수 있다.
- **통합의 용이성**
    - 스프링의 내장 멀티파트 파일 업로드 지원은 애플리케이션에 파일 업로드 기능을 쉽게 통합할 수 있게 한다. 최소한의 설정과 코드 작업으로 `MultipartFile` 인터페이스를 사용하여 파일 업로드 기능을
      구혀할 수 있다.
- **표준화**:
    - 멀티파트/폼-데이터 콘텐츠 타입은 파일 업로드에 널리 사용되는 표준이므로 다양한 클라이언트와 라이브러리와의 호환성이 보장된다.
- **여러 파일 처리**
    - 멀티파트 요청은 한 번의 요청으로 여러 파일 업로드를 처리할 수 있다. 이는 사용자가 여러 파일을 한 번에 업로드할 수 있어 요청 수를 줄이고 사용자 경험을 향상시킨다.
- **메타데이터 및 폼 데이터**
    - 멀티파트 요청은 파일과 함께 추가 폼 데이터를 전송할 수 있다. 이는 파일 설명, 사용자 ID 또는 관련 정보가 함께 요청될 수 있다.

# application.yml

```java
#Maximum file
size allowed
spring.servlet.multipart:
enabled:true
max-file-size:10000MB
  #
Maximum request
size allowed
max-request-size:10000MB
  #
Location where
the files
will be
stored temporarily
location:/Users/temp/uploads
resource:
storage:
local:
root-path:/Users/uploads
```

- 큰 파일인 경우 spring.servlet.multipart.max-file-size 와 max-request-size 두 개를 설정해줘야 한다.
- spring.servlet.multipart.location 은 servlet이 자체적으로 임시로 파일을 생성하는 위치이고, 실제 파일이 저장되는 곳에 저장이 완료되면 임시저장소에서 파일은 자동으로 제거된다.

# 코드

```java

@RequiredArgsConstructor
@RequestMapping("/api/v1/resource")
public class ResourceController {

	@Value("${resource.storage.local.root-path}")
	private String uploadDir;

	@PostMapping("/upload")
	public String uploadResources(@RequestPart(name = "file") MultipartFile file) {
		if (file.isEmpty()) {
			return "File is empty";
		}
		try {
			file.transferTo(Paths.get(uploadDir, file.getOriginalFilename()));
		} catch (IOException e) {
			e.printStackTrace();
			return "Failed to upload file: " + e.getMessage();
		}
		return "success";
	}

```

- `file.transferTo()` 함수는 임시파일저장소에 저장되어 있던 파일이 영구적으로 저장하고자 하는 디렉토리로 이동하는 함수이다.

# spring.servlet.multipart.location

- spring.servlet.multipart.location 으로 설정한 디렉토리 위치에 업로드 요청한 파일이 임시적로 생성이 되면 다음과 같이 .tmp 확장자로 파일이 생성된다.
- 파일 업로드가 모두 완료되면 해당 .tmp 파일은 삭제된다.

![임시저장소에저장된파일](./file/multipartfile_location.png)

## 임시저장소 디렉토리 설정으로 가지는 이점

- **저장 위치 제어**

기본적으로, Spring은 파일 업로드를 처리할 때 시스템의 기본 임시 디렉토리를 사용하지만, 일부 애플리케이션에서는 특정 디렉토리에 파일을 저장해야 할 수
있다. `spring.servlet.multipart.location`을 사용하면 임시 파일의 저장 위치를 명확히 제어할 수 있다.

- 디스크 공간 관리

서버의 디스크 공간을 효율적으로 관리할 수 있다.. 예를 들어, 시스템의 기본 임시 디렉토리의 공간이 제한적이거나 다른 애플리케이션과 공유되는 경우, 별도의 디렉토리를 지정하여 충돌을 피하고 디스크 공간을 더 잘
관리할 수 있다.

- **퍼포먼스 향상**

디스크 I/O 성능을 최적화할 수 있다. 고속의 SSD가 장착된 디렉토리나 특정 스토리지 장치를 사용하여 업로드 성능을 향상시킬 수 있다.

- **보안 및 접근 제어**

보안 및 접근 제어를 강화할 수 있. 특정 디렉토리의 접근 권한을 제한하여 업로드된 파일의 보안을 강화할 수 있다. 기본 임시 디렉토리보다 접근이 엄격하게 제한된 디렉토리를 사용할 수 있다.

- **유지 보수 용이**

특정 디렉토리에 파일을 저장함으로써 로그 파일 관리 및 디버깅이 용이해진다.