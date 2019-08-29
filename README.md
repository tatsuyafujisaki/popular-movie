# 概要
The Movie Database(https://www.themoviedb.org) から映画情報を取得し、表示するアプリです。

映画情報は
* 人気順
* 評価順
* ブックマークのみ

を表示することが可能です。

各映画につき、
* タイトル、公開日、平均投票点、概要
* YouTubeの予告編(複数)
* 批評一覧

を表示できます。

# デモ
以下の操作を行っています。
1. 映画を高評価順に表示
2. 映画を人気順に表示
3. 映画をお気に入りだけ表示 (本例ではスパイダーマンだけ既にお気に入りに入っている)
4. 星印アイコンをタップし、ゴジラをお気に入りに追加
5. ゴジラのポスターをタップし、詳細情報を見る
6. ゴジラのYouTubeの予告編を見る
![screencast.gif](screencast.gif)

# How to run this app
1. Create an account at https://www.themoviedb.org
2. Find your API key at https://www.themoviedb.org/settings/api
3. Append the following to `gradle.properties`
```
TheMovieDatabaseApiKey="Your API key comes here"
```
