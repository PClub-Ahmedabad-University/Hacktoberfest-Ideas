# import required libraries 
import requests 
from bs4 import BeautifulSoup 
from win10toast import ToastNotifier
import time
import schedule
  
# create an object to ToastNotifier class 
n = ToastNotifier()

def get_temp():
    
    url = "https://weather.com/en-IN/weather/today/"

    req = requests.get(url)
    htmldata =  req.text

    soup = BeautifulSoup(htmldata, 'html.parser')
    
    current_temp = soup.find("span", class_= "_3KcTQ")
    # print(current_temp.get_text())

    predict = soup.find("div", class_= "RBVJT")
    # print(predict.get_text())

    result = "Currently : " + current_temp.get_text() + "\n" + predict.get_text()

    print(result)

    n.show_toast("Weather Update",  result, duration = 5)

schedule.every(4).hours.do(get_temp) 

get_temp()

while True:
    schedule.run_pending()
    time.sleep(1)