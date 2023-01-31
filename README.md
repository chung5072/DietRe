# 📗 Dietre: 초보자를 위한 간편한 식단관리 서비스 📗

**SSAFY 7기 자율 프로젝트 A105**

---

<img title="" src="https://user-images.githubusercontent.com/98934673/203674303-78605794-c820-43cc-909a-74573243e9db.png" alt="">

<br><br>
---

## :green_apple: 목차
[1. 서비스 개요](#greenapple-서비스-개요)

[2. 주요 기능](#greenapple-주요-기능)

[3. 사용 기술](#greenapple-사용-기술)

[4. 팀원 소개](#greenapple-팀원-소개)


---

## :green_apple: 서비스 개요

#### 기획 의도

코로나19 이후, 예전과는 달리 젊은 층 사이에서도 건강 관리의 중요성이 확대되고 있다.

건강 관리는 크게 운동과 식단 관리 2가지로 나뉘는데, 운동에 비해 식단 관리는 관심 받기 시작한지 오래되지 않아 상대적으로 널리 알려지지 않았다.

또, 건강 관리가 젊은 층의 자기 관리 영역에 포함되면서 디지털 서비스를 사용하는 경우가 많은데 식단 관리의 경우 관련 지식이 없이도 사용하기 편한 서비스가 있다면 편리할 것 같아 기획하였다.


#### 타겟

> 식단관리의 필요성은 느끼지만 다양한 이유로 처음 시작해보는 초보자

식단관리 경험자들은 디지털 서비스를 특별히 사용하지 않아도 스스로 계산하고 조절할 수 있기에, 식단에 대해 잘 모르고 이제 시작하고자 하는 사람들을 대상으로 삼았다.

1. 건강 관리에 관심을 갖기 시작한 사람
   - 식단 관리를 해야 한다고 생각하지만 정보가 너무 많고 달라서 어떻게 시작해야 하는지 모름
2. 식단 관리를 하고 싶지만 힘들게 하고 싶지는 않은 사람
   - 식단 관리를 하고는 싶지만 식단에 맞는 음식만 만들어 먹기는 힘들고 지쳐서 평소 먹는 식단에서 어떤 부분을 조정해야 하는지 알고 싶음
3. 식단 관리의 필요성을 느끼지만 이것저것 찾아보기는 싫은 사람
   - 식단 관리를 하려고 찾아봤지만 계산하고 챙기기는 귀찮고 편하게 기록하고 관리하고 싶음

초보자들을 크게 3개의 유형으로 분류해 이들에게 적합한 기능이 무엇인지 정리하였다.


#### 필요 기능

> 알아서 계산해주는 맞춤 영양 정보와 쉽고 편한 식단 기록

타겟 사용자의 요구에 맞게 서비스에 필요한 필수 기능을 정리하였다.

1. 사용자 정보에 맞는 맞춤 영양 정보 제공
   - 영양 정보 계산에 필요한 기본 정보와 목표를 지정하면 그에 맞는 맞춤 영양 정보를 제공하고 이 값을 기준으로 비교, 계산한다.
2. 손쉬운 식단 입력 기능
   - 꾸준히 기록하기 위해서는 사용자가 서비스를 사용할 때 귀찮지 않아야 하므로 간편하게 식단을 입력하고 저장한다.
3. 다양한 분석 제공
   - 사람마다 필요한 정보 값의 정도가 다르므로 기록된 값을 다양하게 정리해서 제공한다.

사용자에게 식단을 추천해주는 것에서 그치지 않고, 식사 내역을 정리하고 기록할 수 있는 기능과 그 기록으로 유의미한 정보를 추려내 전달하는 것을 목표로 하였다.

<br>

---

## :green_apple: 주요 기능

1. 초보자가 얻기 어려운 영양 정보를 맞춤형으로 제공

   - 사용자의 신체와 활동, 목표 정보를 받아 그에 맞는 맞춤 영양 제공한다.

2. 번거로운 과정을 최소화하여 꾸준히 기록할 수 있는 기능 제공

   - 인공지능으로 음식 사진을 분석하여 어떤 음식을 먹었는지 파악이 가능하며,

     음식 영양 데이터를 통해 영양소를 얼마나 섭취했는지 그래프 형태로 제공한다.

   - 저장한 식사 데이터는 기록을 통해 확인할 수 있다.

3. 다양한 분석 제공 및 한 눈에 들어오는 확인

   - 사용자가 기록한 내용을 저장하여 기간별 분석 내용을 제공한다.

[화면 구성과 동작](https://lab.ssafy.com/s07-final/S07P31A105/-/blob/master/exec/시연_시나리오.pdf)

실제 화면과 상세 설명은 상단 링크에서 확인할 수 있습니다.

<br>

---

## :green_apple: 사용 기술

#### 아키텍처 구성도

<img title="" src="https://user-images.githubusercontent.com/98934673/203675270-bc44bfc9-5887-4021-bd58-c07547b6dc6e.png" alt="">

<br>

#### FrontEnd

**개발**

Android Studio 2021.2.1.16 Chipmunk

Java 1.8

**Gradle**

`com.android.application` 7.2.2

`om.android.library` 7.2.2

`minSdk` 22

`compileSdk` 32

`targetSdk` 32

**AndroidX(Fragment, ViewModel)**

`androidx.appcompat:appcompat` 1.5.1

`androidx.lifecycle:lifecycle-livedata-ktx` 2.3.1

`androidx.lifecycle:lifecycle-viewmodel` 2.5.0

`androidx.navigation:navigation-fragment` 2.3..5

`androidx.navigation:navigation-ui` 2.3.5

`androidx.viewpager2:viewpager2` 1.0.0

`androidx.legacy:legacy-support-v4` 1.0.0

**Api Call**

`com.squareup.retrofit2:retrofit` 2.9.0

`com.squareup.retrofit2:converter-gson` 2.9.0

**Design**

`com.google.android.material:material` 1.7.0

`me.relex:circleindicator` 2.1.6

`de.hdodenhof:circleimageview` 3.1.0

`com.airbnb.android:lottie` 5.2.0

`com.github.PhilJay:MPAndroidChart` 3.1.0

**Google service**

`com.google.android.gms:play-services-base` 18.1.0

`com.google.android.gms:play-services-auth` 20.3.0

`com.google.android.gms:play-services-plus` 17.0.0

**소셜 로그인**

`naveridlogin_android_sdk` 4.2.6

`com.kakao.sdk` 2.8.3

- 안드로이드 스튜디오에서 자바를 사용해 개발했으며 androidx를 기본으로 다양한 라이브러리를 사용해 개발했다.

- Activity를 최소한으로 사용하고 Fragment를 중심으로 화면을 구성하였으며 데이터를 실시간으로 관찰하고 공유하기 위해 ViewModel을 사용했다.

- 서버와 통신하기 위해 Retrofit2 라이브러리를 사용했다.

- 전체적인 디자인은 가장 최근 지원 버전인 Material 3를 기본으로 사용했고, 다양한 UI를 위해 외부 라이브러리를 활용했다.

- 데이터 그룹을 표현할 때 ListView 대신 RecyclerView를 활용하여 성능 저하를 방지했고,
   공통된 내용을 담을 때는 해당 RecyclerView를 재사용했다.
<br>

#### BackEnd

Java 11

Spring 2.7.5

Spring Security

JWT `io.jsonwebtoken:jjwt:0.9.1`

JPA

Querydsl

MySQL 8.2.8

**Infra**

EC2

S3

Docker

Jenkins

- 회원기능을 JWT로 구현했다.
- Spring Security를 이용해 로그인 관련 필터를 등록하고 인증 처리 수행했다.
- JPA와 Querydsl을 이용해 MySQL에 있는 데이터들을 사용했다.
- 도커를 통해 각 서버의 독립적으로 배포했다.
- 젠킨스로 자동 빌드를 구현했다.
- 이미지를 저장하기 위해 S3를 사용했다.
- EC2 서버에 배포했다.

<br>

### AI

`python>=3.7`

**웹 프레임워크**

`fastapi==0.85.0`

**사용 모델**

[YOLOv7](https://github.com/WongKinYiu/yolov7)

- 이미지에서 객체(object)를 탐지(detection)하기 위한 모델(객체의 종류와 위치를 알 수 있다.)
- yolo는 one-stage모델로 two-stage모델보다 추론(inference)속도가 빠른 특징이 있음

**학습**

- 오픈 소스 yolo모델을 사용
- [AI-Hub](https://www.aihub.or.kr/)에서 400종 50만장 이상의 이미지를 제공받아 학습에 사용

  <br>

---

## :green_apple: 팀원 소개

<img src="https://user-images.githubusercontent.com/98934673/203674245-443f277e-8165-4fde-bdb9-7d759aac38cb.png">
<br><br>
