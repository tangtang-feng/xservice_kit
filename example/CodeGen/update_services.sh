#npm install xservice -g

rm -rf out
xservice -o out -p fleamarket.taobao.com.xservicekitexample -t yaml ServicesYaml


#for file in Services/*.json; do
#done


echo 'Copying oc'
cp -R out/oc/* ../ios/Runner/XService/

echo 'Copying java'
cp -R out/dart/* ../lib/

echo 'Copying java'
cp -R out/java/  ../android/app/src/main/java/fleamarket/taobao/com/xservicekitexample

cd -
