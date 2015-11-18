 ####이 프로젝트는 SockJS를 이용한 안드로이드 채팅 어플리케이션입니다. 당신은 휴대폰 어디에서나 채팅을 할 수 있습니다. 우리는 당신의 채팅을 위해 반투명한 채팅창을 제공하고, 챗해드를 제공합니다. 
 
 
#####Server Github : https://github.com/soma-6th/recipe-api-ml

######우리는 SockJS를 사용하기 위해 Vert.x를 이용한 Java Server Program을 사용했습니다. The project is connected with this server. You can easily set up the project.


	//util/AppSetting.java

	public class AppSetting {

    private static final String BASE_URL = "http://133.130.113.101";
    public static final String REST_URL = BASE_URL + ":7010";
    public static final String SOCKJS_URL = BASE_URL + ":7030";
}




###SockJS Chat on Activity
![Chat](https://cloud.githubusercontent.com/assets/8899510/11249234/08dd980c-8e69-11e5-9bc7-6edd5594aa7f.jpeg)


###Chathead
![Chat](https://cloud.githubusercontent.com/assets/8899510/11249333/84dce41c-8e69-11e5-8720-de1ae58b57db.png)


###SockJS Chat on Chathead Service
![Chat](https://cloud.githubusercontent.com/assets/8899510/11249241/0deb1374-8e69-11e5-87fb-44800895f35f.png)


###With COC (We provide optimized icon on the app.)
![coc chat](https://cloud.githubusercontent.com/assets/8899510/11249222/f8fc9dac-8e68-11e5-99f1-4731516ab72d.jpeg)


#License and Copyright Notices
##This application is licensed under Apache License, Version 2.0


	Copyright 2015 Software Maestro 6th GGamTalk Team

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
