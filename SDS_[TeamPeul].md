
Slangguage

























22012104	윤선중 (팀장 / sunsun1080@gmail.com)

22211993	김민찬

22213496	박상윤

22012148	서정빈

22012109	김민우

22113640	김동현

22112060	김찬명

[ Revision history ]


Revision date

Version #

Description

Author

2025/10/31

1\.0

Initial version

윤선중
























= Contents =




1. Introduction ......................................................................................

1. Use case analysis ............................................................................

1. Class diagram ........................................................................................

1. Sequence diagram ..................................................................................

1. State machine diagram ............................................................................

1. User interface prototype ...............................................................

1. Implementation requirements ...............................................................

1. Glossary ................................................................................................

1. References ............................................................................................



= Authors for each section =

Introduction – 윤선중

Use case analysis – 윤선중

Class diagram – 박상윤, 서정빈

Sequence diagram – 김민우, 김동현

State machine diagram – 김찬명, 김민찬

User interface prototype - 윤선중

Implementation requirements – 박상윤, 서정빈

Glossary – 김민우, 김동현

References – 김찬명, 김민찬

1. Introduction

본 문서는 Java와 Swing으로 구현한 문장 중심 한국어 학습 애플리케이션 ‘슬랭귀지(Slangguage)’의 소프트웨어 설계 명세를 체계적으로 정리한 것이다. 프로젝트의 목적과 범위, 대상 사용자, 개발 환경을 시작으로, 회원가입과 로그인, 언어쌍 선택(KO→EN/JP/CN), 슬랭 모드, 문장 기반 4지선다 문제 출제, 힌트와 해설, 저장 후 초기 화면 복귀, 사용자별 랭킹(Top5), 최근 10일 정확도 분석 등 핵심 기능 요구사항을 기술한다. 또한 성능(문항 전환 200ms 이내, 초기 로딩 1초 내), 국제화(UTF-8과 CJK 폰트 폴백), 보안(SHA-256 해시, CSV 주입 방지), 신뢰성(임시 파일 후 rename을 통한 원자적 저장), 유지보수성(계층화와 의존 최소화) 같은 비기능 요구사항을 명확히 정의한다. 도메인 모델(UserProfile, SentenceItem, LangPair, Stats)과 서비스 계층(CSVLoader, AuthService, ChoiceGenerator, ProgressSaver, Score/Analytics), 프레젠테이션 계층(MainFrame과 각 패널)의 책임과 인터페이스를 서술하고, 대표 유스케이스, 클래스·시퀀스·상태 기술, 표준화된 CSV 스키마와 예외 처리, 선택지 생성 및 SM-2 기반 복습 스케줄링 알고리즘을 포함한다. 마지막으로 단위·통합 테스트 계획(200건 이상), 위험 관리, 배포 및 운영 지침을 제시해 팀 협업과 품질 보증의 기준을 제공한다.

설계의 핵심은 문장 우선 학습과 슬랭·일반 데이터의 엄격한 분리에 있다. 모든 문제는 한국어 문장에 빈칸을 두고 출제되며, 선택지는 중복과 의미적 근접을 줄이는 규칙으로 자동 생성되어 학습 효율과 난이도 균형을 확보한다. isSlang 플래그와 언어쌍 필터로 슬랭 모드와 일반 모드를 명확히 구분해 일관된 경험을 보장하고, UTF-8 CSV 중심의 결정적 데이터 모델을 채택해 재현 가능한 빌드와 간편한 코퍼스 확장을 지원한다. 아키텍처는 UI→Service→Model 계층 구조로 관심사를 분리하여 테스트 용이성과 팀 병렬 개발성을 높였고, 저장은 임시 파일 작성 후 원자적 교체로 데이터 무결성을 확보했다. 국제화 이슈는 폰트 폴백과 방어적 파싱으로 예방하며, 학습 결과는 랭킹과 10일 정확도 차트로 시각화해 데이터 기반 피드백 루프를 형성한다. 슬랭귀지는 단순 암기를 넘어 실제 문장 맥락 속 선택과 반복을 통해 외국인 학습자의 한국어 어휘·표현(특히 슬랭)에 대한 이해와 장기 기억화를 동시에 달성하도록 설계되었다.





1. Use case analysis

1. Use case Diagram


1. Use case Description

Use case #1 : Configure Study Settings

GENERAL CHARACTERISTICS

Summary

로그인한 사용자가 학습 전 언어쌍(KO→EN/JP/CN)과 Slang Mode를 설정한다.

Scope

Slangguage

Level

User level

Author

윤선중

Last Update

2025\. 11. 01.

Status

Analysis (Finalize)

Primary Actor

User

Preconditions

로그인 완료, 리소스 접근 가능

Trigger

홈 화면에서 학습 설정을 변경/저장하려고 할 때

Success Post Condition

선택된 Language Pair와 Slang Mode가 프로필/세션에 반영되고 지속된다.

Failed Post Condition

설정이 저장되지 않으며 이전 값이 유지된다.

MAIN SUCCESS SCENARIO

Step

Action

S

사용자가 홈 화면에서 설정 구성을 시작한다.

1

사용자는 Language Pair(KO→EN/JP/CN)를 선택한다.

2

사용자는 Slang Mode를 ON/OFF로 토글한다.

3

시스템은 변경 사항을 검증하고 저장 버튼을 활성화한다.

4

사용자가 저장을 클릭한다.

5

시스템은 설정을 영속화(profile/session)하고 확인 메시지를 표시한다.

6

이 use case는 성공하면 끝난다.

EXTENSION SCENARIOS

Step

Branching Action

3

3a. 슬랭 데이터 부족이 감지됨 → 실패

…3a1. 시스템이 “해당 조합의 슬랭 문항 부족, 일반 모드 권장” 메시지를 보여준다.

...3a2. 사용자가 일반 모드로 전환하거나, 다른 Language Pair를 선택한다(3단계로 복귀).

4

4a. 파일/권한 문제로 저장 불가 → 실패

…4a1. 오류 메시지 표시 후 재시도/취소 선택 제공.

...4a2. 취소 선택 시 기존 설정 유지.

RELATED INFORMATION

Performance

≤ 1 seconds

Frequency

세션 시작 시 1회, 필요 시 추가 조정

<Concurrency>

단일 사용자/로컬

Due Date

2025\.11.01.



Use case #2 : Start New Session

GENERAL CHARACTERISTICS

Summary

설정에 맞는 새 학습 세트를 구성하고 첫 문항을 표시한다.

Scope

Slangguage

Level

User level

Author

윤선중

Last Update

2025\. 11. 01.

Status

Analysis (Finalize)

Primary Actor

User

Preconditions

로그인 완료, 리소스 접근 가능

Trigger

사용자가 “Start New Session”을 클릭할 때

Success Post Condition

첫 문항(Q1)이 렌더링된다.

Failed Post Condition

세션 생성 실패, 홈으로 복귀 또는 재시도 유도.

MAIN SUCCESS SCENARIO

Step

Action

S

사용자가 새 학습 시작을 요청한다.

1

시스템은 sentences.csv를 로드/파싱한다

2

시스템은 Language Pair/Slang Mode 기준으로 문항 풀을 필터링한다.

3

시스템은 시드 기반 셔플로 문항 순서를 결정한다.

4

시스템은 Q1을 렌더링한다.

5

이 use case는 성공하면 끝난다.

EXTENSION SCENARIOS

Step

Branching Action

1

1a. 파일 없음/인코딩 오류/파싱 실패 → 실패

…1a1. 오류 메시지와 재시도/가이드(인코딩·CSV 포맷) 표시.

...1a2. 재시도 시 1단계로 복귀.

2

2a. 필터 결과가 0건 → 실패

…2a1. 시스템이 설정 변경을 안내하고 설정 화면으로 이동 제안.

RELATED INFORMATION

Performance

≤ 1 seconds

Frequency

매 학습 세션마다 1회

<Concurrency>

단일 사용자/로컬

Due Date

2025\.11.01.



Use case #3 : Resume Saved Session

GENERAL CHARACTERISTICS

Summary

이전에 저장된 진행 상태에서 학습을 재개한다.

Scope

Slangguage

Level

User level

Author

윤선중

Last Update

2025\. 11. 01.

Status

Analysis (Finalize)

Primary Actor

User

Preconditions

유효한 저장 스냅샷 존재

Trigger

사용자가 “Resume”을 클릭할 때

Success Post Condition

저장 지점부터 다음 문항이 표시된다.

Failed Post Condition

복원 실패, 새 학습 시작을 유도.

MAIN SUCCESS SCENARIO

Step

Action

S

사용자가 이어하기를 선택한다.

1

시스템은 저장 파일을 찾고 무결성을 검사한다.

2

시스템은 진행률/남은 문항/통계를 복원한다.

3

시스템은 다음 문항을 렌더링한다.

4

이 use case는 성공하면 끝난다.

EXTENSION SCENARIOS

Step

Branching Action

1

1a. 파일 손상/버전 불일치 → 실패

…1a1. 자동 복구 시도 후 실패 시 경고 메시지.

...1a2. 새 학습 시작으로 전환 안내.

RELATED INFORMATION

Performance

≤ 1 seconds

Frequency

사용자에 따라 가끔

<Concurrency>

단일 사용자/로컬

Due Date

2025\.11.01.


Use case #4 : Solve MCQ

GENERAL CHARACTERISTICS

Summary

문장 기반 4지선다 문제를 풀이하고 피드백을 받고 다음 문제로 이동한다.

Scope

Slangguage

Level

User level

Author

윤선중

Last Update

2025\. 11. 01.

Status

Analysis (Finalize)

Primary Actor

User

Preconditions

문항과 4개 선택지가 화면에 렌더링됨

Trigger

사용자가 보기 클릭 또는 1~4/Enter 입력 시

Success Post Condition

정오 판정 반영, 통계/스케줄러 업데이트, 다음 문항 준비

Failed Post Condition

(없음) — 단, 치명 오류 시 세션 중단/저장 유도

MAIN SUCCESS SCENARIO

Step

Action

S

사용자가 현재 문항을 풀이한다.

1

사용자는 보기(클릭 또는 1~4/Enter)를 선택한다.

2

시스템은 정답 포함 여부를 검증한다.

3

시스템은 시각적 피드백(정답/오답)을 즉시 표시한다.

4

시스템은 SM-2 변형 스케줄러와 통계를 갱신한다.

5

시스템은 다음 문항을 로드한다.

6

이 use case는 성공하면 끝난다.

EXTENSION SCENARIOS

Step

Branching Action

1

1a. View Hint 요청 → 분기

…1a1. 품사/앞 음절 등의 단서를 표시한다(감점/쿨타임 정책 선택적).

1b. View Explanation 요청 → 분기

…1b1. 정오답 해설, 용례, 주의 표현을 표시한다.

1c. Skip Question 요청 → 분기

…1c1. 현재 문항을 미채점 기록으로 남기고 다음 문항으로 이동한다.

2

2a. 선지 중복/정답 누락 감지 → 실패

…2a1. 시스템이 즉시 선택지 재생성 후 안내 메시지를 표시한다.

RELATED INFORMATION

Performance

≤ 1 seconds

Frequency

세션 동안 반복

<Concurrency>

단일 사용자/로컬

Due Date

2025\.11.01.


Use case #5 : Save and Return Home

GENERAL CHARACTERISTICS

Summary

현재 진행 상태를 안전하게 저장하고 홈으로 복귀한다.

Scope

Slangguage

Level

User level

Author

윤선중

Last Update

2025\. 11. 01.

Status

Analysis (Finalize)

Primary Actor

User

Preconditions

학습 세션 진행 중

Trigger

사용자가 “Save & Home”을 클릭할 때

Success Post Condition

진행률/남은 문항/통계 저장, 홈 화면 표시

Failed Post Condition

저장 실패, 세션 유지 상태로 복귀

MAIN SUCCESS SCENARIO

Step

Action

S

사용자가 저장 후 홈 복귀를 요청한다.

1

시스템은 임시 파일에 현재 상태를 기록한다.

2

시스템은 rename으로 원자적 교체를 수행한다.

3

시스템은 저장 성공 메시지와 함께 홈으로 이동한다.

4

이 use case는 성공하면 끝난다.

EXTENSION SCENARIOS

Step

Branching Action

2

2a. 저장 실패(권한/디스크) → 실패

…2a1. 시스템이 즉시 선택지 재생성 후 안내 메시지를 표시한다.

…2a2. 재시도 선택 시 1단계로 복귀.

RELATED INFORMATION

Performance

≤ 1 seconds

Frequency

세션 중 끔

<Concurrency>

단일 사용자/로컬

Due Date

2025\.11.01.


Use case #6 : View Leaderboard

GENERAL CHARACTERISTICS

Summary

상위 5명의 맞힌 수/배지/진행 바를 확인한다.

Scope

Slangguage

Level

User level

Author

윤선중

Last Update

2025\. 11. 01.

Status

Analysis (Finalize)

Primary Actor

User

Preconditions

점수 데이터 존재

Trigger

사용자가 “Leaderboard”를 연다.

Success Post Condition

Top 5가 시각적으로 표시된다.

Failed Post Condition

데이터 없음 시 자리표시자와 안내 메시지 표시

MAIN SUCCESS SCENARIO

Step

Action

S

사용자가 랭킹 화면을 연다.

1

시스템은 상위 5명 데이터를 조회한다.

2

시스템은 동점 규칙을 적용해 정렬한다.

3

시스템은 카드/배지/진행 바 UI를 렌더링한다.

4

이 use case는 성공하면 끝난다.

EXTENSION SCENARIOS

Step

Branching Action

1

1a. 데이터 부족/파일 없음 → 실패

…1a1. 자리표시자와 “학습을 시작해 랭킹에 도전하세요” 메시지 표시

RELATED INFORMATION

Performance

≤ 1 seconds

Frequency

세션 중 끔

<Concurrency>

단일 사용자/로컬

Due Date

2025\.11.01.


Use case #7 : View Analytics

GENERAL CHARACTERISTICS

Summary

최근 10일 일자별 정확도와 오답 TOP N을 확인한다.

Scope

Slangguage

Level

User level

Author

윤선중

Last Update

2025\. 11. 01.

Status

Analysis (Finalize)

Primary Actor

User

Preconditions

이벤트 로그 존재

Trigger

사용자가 “Analytics”를 연다.

Success Post Condition

라인차트와 오답 목록 표시

Failed Post Condition

데이터 없음 안내 및 학습 유도

MAIN SUCCESS SCENARIO

Step

Action

S

사용자가 분석 화면을 연다.

1

시스템은 최근 10일 데이터를 집계한다(결측일 0% 보정).

2

시스템은 정확도 라인차트를 렌더링한다.

3

시스템은 오답 TOP N을 표시한다.

4

이 use case는 성공하면 끝난다.

EXTENSION SCENARIOS

Step

Branching Action

1

1a. 로그 없음 → 실패

…1a1. “아직 데이터가 없습니다” 메시지와 학습 시작 버튼 제공.

RELATED INFORMATION

Performance

≤ 1 seconds

Frequency

세션 중 끔

<Concurrency>

단일 사용자/로컬

Due Date

2025\.11.01.


Use case #8 : Login

GENERAL CHARACTERISTICS

Summary

사용자가 ID/PW로 인증해 개인화된 홈에 진입한다.

Scope

Slangguage

Level

User level

Author

윤선중

Last Update

2025\. 11. 01.

Status

Analysis (Finalize)

Primary Actor

User

Preconditions

계정 존재

Trigger

로그인 화면에서 로그인 버튼 클릭 시

Success Post Condition

인증 세션 성립, “{id}님 안녕하세요!” 표시

Failed Post Condition

인증 실패, 로그인 단계 유지

MAIN SUCCESS SCENARIO

Step

Action

S

사용자가 로그인한다.

1

사용자는 ID와 PW를 입력하고 로그인 버튼을 누른다

2

시스템은 PW 해시를 검증한다.

3

일치하면 세션을 생성하고 홈 화면으로 이동한다.

4

이 use case는 성공하면 끝난다.

EXTENSION SCENARIOS

Step

Branching Action

2

2a. 불일치/빈 값/파일 잠금 → 실패

…2a1. 오류 메시지와 재시도 제공(필요 시 지연·회수 제한).

…2a2. 재시도 선택 시 1단계로 복귀.

RELATED INFORMATION

Performance

≤ 1 seconds

Frequency

세션 중 끔

<Concurrency>

단일 사용자/로컬

Due Date

2025\.11.01.


Use case #9 : Sign Up

GENERAL CHARACTERISTICS

Summary

신규 사용자가 계정을 생성한다.

Scope

Slangguage

Level

User level

Author

윤선중

Last Update

2025\. 11. 01.

Status

Analysis (Finalize)

Primary Actor

User

Preconditions

고유 ID 미사용

Trigger

회원가입 화면에서 제출 버튼 클릭 시

Success Post Condition

users.csv에 계정(해시) 저장, 로그인 화면으로 이동

Failed Post Condition

계정 생성 실패, 입력 단계 유지

MAIN SUCCESS SCENARIO

Step

Action

S

사용자가 회원가입을 진행한다.

1

사용자는 ID/PW를 입력하고 제출한다.

2

시스템은 형식 검증과 중복 검사를 수행한다.

3

시스템은 PW 해시를 저장하고 계정을 생성한다.

4

시스템은 로그인 화면으로 전환한다.

5

이 use case는 성공하면 끝난다.

EXTENSION SCENARIOS

Step

Branching Action

2

2a. 약한 PW/중복 ID → 실패

…2a1. 구체적 오류 메시지와 재입력 유도.

3

3a. 파일 쓰기 실패 → 실패

…3a1. 오류 메시지 및 재시도/취소 제공.

RELATED INFORMATION

Performance

≤ 1 seconds

Frequency

세션 중 끔

<Concurrency>

단일 사용자/로컬

Due Date

2025\.11.01.

1. Class diagram


[그림 3-1] 전체 Class diagram

클래스명

속성(Attributes)

메서드(Methods)

설명(Description)

UserProfile

username: String

level: int

saveProfile()

사용자 정보와 학습 진행 상태 관리

LangPair

sourceLang: String

targetLang: String

setLanguage()

학습 언어쌍 관리

Stats

accuracy: double

timeSpent: int

updateStats()

학습 정확도와 시간 통계 관리

SentenceItem

text: String

getSentence()

문장 데이터를 제공

Word

term: String

meaning: String

getMeaning()

단어 정보와 뜻 관리

ChoiceGenerator

generateChoices()

문제 보기 생성

AnswerChecker

checkAnswer()

정답 확인 기능

CSVLoader

loadCSV()

CSV 파일 로드

EventLogger

logEvent()

학습 이벤트 기록

ProgressSaver

saveProgress()

학습 진행도 저장

ScoreService

entry: int

calculateScore()

점수 계산

FontUI

applyFont()

글꼴 스타일 적용

LeaderboardPane

showLeaderboard()

순위판 표시

LoginDialog

authenticateUser()

로그인 처리

SignupDialog

registerUser()

회원가입 처리

UIUI

renderUI()

UI 렌더링

ThemeUI

changeTheme()

테마 변경

PillChoiceButton

selectChoice()

보기 선택 UI

Main

startApp()

프로그램 시작

AnalyticsService

analyzeResults()

결과 분석 서비스

AuthService

login(), signup()

인증 서비스

MainFrame

currentPane: Pane

switchPane(), showStats()

화면 전환과 통계 표시

FeedbackPane


피드백 화면

StartPane


시작 화면

QuestionPane


문제 풀이 화면

ChoicePane


선택지 화면

AnalyticsPane


분석 화면

QuestionSchedule

nextQuestion()

문제 순서 관리

SM2Schedule

generateCard()

반복학습 알고리즘

WrongBiasSchedule

retryWrong()

오답 재학습 관리

QuizItem

question: String

answer: String

checkAnswer()

문제 데이터 클래스

Session

currentUser: UserProfile

saveSession()

사용자 세션 관리



1. Sequence diagram




[그림 4-1] 전체 Sequence diagram


[그림 4-2] 학습 세션 초기화 및 첫 퀴즈 출제


순서

송신자 → 수신자

메시지

코드/기능 대조

1

User → StartPanel

click(StartButton)

사용자가 UI(StartPanel)의

'학습 시작' 버튼을 클릭합니다.

2

StartPanel → Main

startLearning()

Main.java의 start.getStartBtn() 블록이 실행됩니다.

이는 학습 시작 이벤트를 Main 컨트롤러에 전달합니다.

3

Main → Main

Filter/Load Items

Main.java에서 UserProfile의 LangPair 및

isSlangMode 설정을 기반으로 sentences 리스트를 순회하며

학습 Pool을 구성합니다.

4

Main →SM2Scheduler

init(items)

Main.java에서 QuestionScheduler

qs = new SM2Scheduler();를 생성한 후,

qs.init(items);를 호출합니다.

이는 SM2Scheduler.java에서

학습 Pool을 기반으로 각 문항의 학습 주기 정보를 초기화합니다.

5

Main → QuizPanel

update(stats)

Main.java에서 sPanel.update(stats);를 호출합니다.

이는 Stats.java 객체의 현재 통계 값을 가져와 화면의 통계 패널

(StatsPanel은 QuizPanel 내에 포함된 것으로 간주)에 갱신합니다.

6

Main →SM2Scheduler

next()

Main.java에서 current[0] = qs.next();를 호출합니다.

이는 SM2Scheduler.java의 로직에 따라

다음 출제할 퀴즈 아이템을 결정하여 반환 요청합니다.

7

SM2Scheduler→ Main

return currentQuizItem

SM2Scheduler.java의 next() 메서드가 결정한 QuizItem 객체

(문항)를 반환합니다.

8

Main → QuizPanel

setItem(current)

Main.java에서 qPanel.setItem(current[0]);를 호출합니다.

이는 퀴즈 문항 내용을 화면의 QuestionPanel에 표시합니다.

9

Main → ChoiceGenerator

makeChoices(current, pool)

Main.java의 if (current[0] instanceof SentenceItem)

조건문 내에서

choice.setChoices(cg.makeChoices((SentenceItem)

current[0], pool));의 일부로 실행됩니다.

ChoiceGenerator.java는 정답과 유사한 오답을 포함한

선택지 4개를 생성합니다.

10

ChoiceGenerator → Main

return choices

ChoiceGenerator.java가 생성한 선택지 리스트를 반환합니다.

11

Main → QuizPanel

setChoices(choices)

Main.java에서 choice.setChoices(...)를 호출하여,

생성된 선택지들을 화면의 ChoicePanel에 표시합니다.

12

Main → QuizPanel

showQuiz()

Main.java에서 frame.showQuiz();를 호출하여

메인 프레임을 퀴즈 학습 모드로 전환하고,

사용자 입력을 기다립니다.



[그림 4-3] 퀴즈 응답 및 순환


순서

송신자 → 수신자

메시지

코드/기능 대조

13

User → QuizPanel

submit(pick)

사용자가 ChoicePanel의 'Submit' 버튼을 클릭하거나

선지를 선택합니다.

14

QuizPanel → Main

getSelection()

Main.java의

choice.getSubmitButton().addActionListener(...)

블록이 실행됩니다. Main은 사용자가

선택한 답(pick)을 가져옵니다.

15

Main → AnswerChecker

check(answer, pick)

단순 문자열 비교를 통해 정답 여부(ok)를 결정합니다.

16

AnswerChecker → Main

return ok (정답 여부)

정규화된 정답 비교 결과(boolean ok)를 반환합니다.

17

Main → Stats

record(current, ok)

Main.java에서 stats.record(current[0], ok);를 호출합니다.

Stats.java는 총 시도, 정답, 연속 정답 횟수를 갱신합니다.

18

[ok==true]→ ScoreService

addCorrect(username, 1)

선택적 실행 (Opt):Main.java의

if (ok && user[0] != null)

scoreService.addCorrect(user[0].getUsername(), 1);

로직이 실행됩니다. ScoreService.java는 사용자의

누적 점수를 1 증가시킵니다.

19

Main → EventLogger

log(username, itemId, ok)

Main.java에서 eventLogger.log(...)를 호출합니다.

EventLogger.java는 퀴즈 시도 기록을

events.csv에 추가합니다.

20

Main → SM2Scheduler

feedback(current, ok)

Main.java에서 qs.feedback(current[0], ok);를 호출합니다.

SM2Scheduler.java는 SM-2 알고리즘에 따라

문항의 난이도와 다음 학습 간격을 재조정합니다.

21

Main → QuizPanel

showFeedback(ok, answer)

Main.java에서 fPanel.showFeedback(...)를 호출하여

사용자에게 정답/오답 결과와 실제 정답을 알려줍니다.

22

Main → QuizPanel

update(stats)

Main.java에서 sPanel.update(stats);를 호출하여

갱신된 통계(Stats)를 화면에 표시합니다.

23

Main → SM2Scheduler

next()

순환의 시작:Main.java에서 current[0] = qs.next();를 호출하여

다음 퀴즈 아이템을 요청합니다.

24

SM2Scheduler → Main

return nextQuizItem

스케줄러가 결정한 다음 퀴즈 아이템을 반환합니다.

25

[nextQuizItem is null]→ User

showMessage(학습 종료)

대안 실행 (Alt):Main.java에서 if (current[0] == null)

조건문이 만족되면 학습이 종료되었음을 알리는

메시지를 표시합니다.

26

[Break]→ QuizPanel

setItem(next)

루프 계속: 다음 퀴즈 아이템(next)이 존재할 경우,

이전 단계 (8~11번)와 동일하게 다음 퀴즈를 화면에

표시하는 과정을 시작합니다.

27

[Break]→ChoiceGenerator

makeChoices(next, pool)

다음 퀴즈에 대한 새로운 선택지를 생성합니다.

28

ChoiceGenerator →Main

return choices

생성된 선택지를 반환합니다.

29

Main → QuizPanel

setChoices(choices)

다음 퀴즈의 선택지들을 화면에 표시하고,

순환을 계속하기 위해 사용자 입력을 기다립니다.



[그림 4-4] 학습 저장 및 종료


순서

송신자 → 수신자

메시지

코드/기능 대조

30

User → Main

click(SaveExitButton)

사용자가 '저장 및 종료' 버튼을 클릭합니다.

Main.java의 saveExit.addActionListener 블록이 실행됩니다.

31

Main →ProgressSaver

save(user, stats, currentId)

Main.java는 ProgressSaver 객체를 생성하고

.save(user[0], stats, current[0].getId())를 호출합니다.

ProgressSaver.java는 UserProfile, Stats,

마지막 QuizItem의 ID를 파일에 저장합니다.

32

Main → Main

Clear State

Main.java는 clear.run()을 호출하여 메모리에 로드된

Stats 변수들을 초기화합니다 (예: stats.total = 0).

33

Main → QuizPanel

showStart()

Main.java는 frame.showStart()를 호출하여

메인 프레임의 화면을 초기 시작 화면으로 전환합니다.


1. State machine diagram

1. 개요

본 다이어그램은 슬랭(은어) 학습을 위한 퀴즈 애플리케이션의 사용자 흐름을 \*\*상태(state)\*\*와 전이(transition) 관점에서 모델링한 것이다. 시스템은 Main Panel을 중심으로 회원관리(회원가입/로그인), 학습 설정(언어·슬랭 모드), 리더보드(개인 기록), 분석(이력 조회), 그리고 퀴즈 수행으로 구성된다. 각 상태는 entry/exit/do 동작을 통해 화면 표시와 로직 실행을 분리해 기술하였고, 이벤트(예: Select ‘학습 시작’)와 결과(예: Success / Fail)에 의해 전이가 일어난다.




[그림 5-1] 전체 State machine diagram





1. 상위 구조와 화면 배치

Main Panel(중심 허브)

모든 기능의 진입점이다. 사용자는 여기서 회원가입/로그인, 설정 변경, 분석 보기, 퀴즈 시작을 선택한다.

오른쪽 사이드 패널의 LeaderBoard(로그인 성공 시)

Login —(Success)→ LeaderBoard 전이 후 do/Start LeaderBoard Panel 동작이 수행되어, 개인·전체 순위를 사이드 영역에 표시한다. 이로써 학습 흐름을 방해하지 않고 성취 피드백을 제공한다.

하위 복합 상태(Composite State) Quiz

퀴즈는 별도의 서브머신으로 묶여 있으며, 내부에 Question Panel 상태와 정답/오답 전이가 반복적으로 순환한다.

1. 주요 상태 상세

1. 회원 관리

Register

entry/Show RegisterDialogue: 회원가입 다이얼로그 표시.

Success 시 Main Panel로 복귀, Fail 시 상태 유지(유효성 오류, 중복 아이디 등).

Login

entry/Show LoginDialogue: 로그인 다이얼로그 표시.

Success 시 LeaderBoard 패널 기동, Fail 시 재시도.

확장 제안: 가드 조건 [authOK] / [authFail] 명시 및 잠금 지연(backoff) 정책 모델링.

1. 설정·프로필

UserProfile

do/setSlangMode, do/setPreferredLangPair: 사용자가 슬랭 모드(예: 일반/헤비 등)와 언어쌍(예: EN↔KR)을 지정한다.

설계 포인트: 설정 값은 퀴즈 문제 생성기의 파라미터로 전달되어 난이도·유형을 결정한다.

1. 분석 기능

Analytic Service

entry/Show AnalyticsPanel: 기간별 정답률, 시도 수, 평균 소요시간, 스트릭(연속 정답) 등의 누적 지표를 시각화한다.

UX 포인트: 학습 중단 없이 Main Panel로 즉시 복귀 가능한 단방향 전이 채택.

1. 리더보드

LeaderBoard

do/Start LeaderBoard Panel: 로그인 후 자동으로 로드. 개인 최고 기록과 글로벌 랭크를 제공하여 동기부여를 강화한다.

성능 고려: 비동기 로딩을 암시하는 do/ 동작으로 모델링하여 UI 블로킹을 회피.

1. 퀴즈(Composite State)

진입 동작: Main Panel —(Select '학습 시작')→ Quiz

퀴즈 모듈이 설정값을 읽어 문제 세션을 초기화한다.

Question Panel

entry/Correct = 0, Total = 0, Streak = 0 초기화(다이어그램 상 entry가 분리 표기).

사용자가 응답할 때마다 다음 전이 수행:

정답: Correct/ Correct++, Streak++

오답: inCorrect/ Streak = 0

공통 종료: exit/ Total++로 시도 수를 누적.

세션 종료: Select ‘저장하고 나가기’ 이벤트로 퀴즈 상태를 빠져나오며 기록을 영속화한다(예: 로컬 DB·클라우드 동기화).

품질 속성: 내부 루프가 self-transition 중심으로 설계되어 문제-평가-집계 사이클이 단순·안정적이다.


1. User interface prototype





`		 `[그림 6-1] 초기화면

위 그림은 프로그램의 초기화면이다. 학습 언어 쌍 선택, 모드선택, 회원가입, 로그인, 새 학습 시작, 분석 등의 기능들을 확인할 수 있다.






`	          `[그림 6-2] 회원가입 화면

위 그림은 회원가입 화면이다. 사용자명과 비밀번호, 선호하는 언어 쌍을 선택하여 간단하게 회원가입을 진행할 수 있다.



`		  `[그림 6-3] 로그인 화면

위 그림은 로그인 화면이다. 회원가입 시에 등록하였던 사용자명과 비밀빈호를 입력하면 분리된 개별의 계정으로 학습이 가능하다.





`	    `[그림 6-4] 랭킹 화면

위의 그림은 유저들의 상위 Top5 랭킹을 확인할 수 있는 화면이다. 맞힌 문제의 수를 기준으로 순위가 결정되며 절대적 학습량을 높이기 위한 취지이다.








[그림 6-5] 학습분석 화면

위 그림은 최근 학습 정확도를 나타내는 화면이다. 최근 학습을 기록하며 정확도를 기준으로 사용자의 실력을 가시적으로 볼 수 있다.






[그림 6-6] 영문 일반학습 화면

위 그림은 영문 사용자가 일반학습을 진행하는 화면이다. 빈칸의 들어가야 할 알맞은 한국어를 4가지 보기 중 1개를 선택하여 정답을 제출해야한다.








[그림 6-7] 일문 일반학습 화면

위 그림은 일문 사용자가 일반학습을 진행하는 화면이다. 빈칸의 들어가야 할 알맞은 한국어를 4가지 보기 중 1개를 선택하여 정답을 제출해야한다.








[그림 6-8] 중문 일반학습 화면

위 그림은 중문 사용자가 일반학습을 진행하는 화면이다. 빈칸의 들어가야 할 알맞은 한국어를 4가지 보기 중 1개를 선택하여 정답을 제출해야한다.








[그림 6-9] 영문 슬랭학습 화면

위 그림은 영문 사용자가 슬랭학습을 진행하는 화면이다. 빈칸의 들어가야 할 알맞은 한국어를 4가지 보기 중 1개를 선택하여 정답을 제출해야한다.









[그림 6-10] 일문 슬랭학습 화면

위 그림은 일문 사용자가 슬랭학습을 진행하는 화면이다. 빈칸의 들어가야 할 알맞은 한국어를 4가지 보기 중 1개를 선택하여 정답을 제출해야한다.









[그림 6-11] 중문 슬랭학습 화면

위 그림은 중문 사용자가 슬랭학습을 진행하는 화면이다. 빈칸의 들어가야 할 알맞은 한국어를 4가지 보기 중 1개를 선택하여 정답을 제출해야한다.










[그림 6-12] 저장 후 되돌아가기 화면

위 그림은 저장 후 되돌아가는 화면이다. 문제 풀이를 중지하고 초기화면으로 돌아간다.


1. Implementation requirements

H/W platform requirements

- Processor: x86\_64 Dual-Core+
- RAM: 4GB+
- Storage: ≥ 200MB
- Network: 불필요(로컬 동작)

S/W platform requirements

- OS Windows 10/11 권장 (macOS 12+ / Ubuntu 22.04+)
- JDK 1.8.0+
- JRE 1.8.0+

8\. Glossary


이름

설명

MCQ

Multiple Choice Question

슬랭(Slang)

한국어의 비속어, 줄임말, 신조어 등을 나타냄

SM2

SuperMemo-2 간격반복 알고리즘


9\. References

SuperMemo 2: Algorithm

https://super-memory.com/english/ol/sm2.htm

JFreeChart Developer Guide (자바 데스크톱 차트)

https://www.jfree.org/jfreechart/

Oracle Java Tutorials – Swing

https://docs.oracle.com/javase/tutorial/uiswing/
