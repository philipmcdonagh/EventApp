import sys
import logging
import pymysql
from datetime import datetime
import time

rds_host  = "mysqlforlambdatest.cemvd4wqsrbf.eu-west-1.rds.amazonaws.com"
name = "philbot"
password = "EvnTgqsEBtKKkEKaYqcG12!"
db_name =  "EventDB"


logger = logging.getLogger()
logger.setLevel(logging.INFO)


try:
    conn = pymysql.connect(rds_host, user=name, passwd=password, db=db_name, connect_timeout=5)    
except:  
    logger.error("ERROR: Unexpected error: Could not connect to MySql instance.")
    sys.exit()

print("SUCCESS: Connection to RDS mysql instance succeeded")
logger.info("SUCCESS: Connection to RDS mysql instance succeeded")

item_count = 0
checkinCount = 0
checkoutCount = 0
checkin = False
checkout = False

with conn.cursor() as cur:
    cur.execute('select * from Event order by eventTime asc')
    conn.commit()       
    for row in cur:
        if item_count == 0 and row[0] == 'checkout':
            print("The first row is a checkout, discard",item_count)            
        elif row[0] == 'checkin':  
            checkin = True   
            checkinTime = row[2]
            checkinCount += 1                                  
        elif row[0] == 'checkout':
            checkout = True 
            checkoutTime = row[2]          
            checkoutCount += 1            
        if checkin and checkout:
            print("---------------------------")
            print("checkin time: ",checkinTime)
            print("checkout time: ",checkoutTime)
            timeDiff = checkoutTime - checkinTime
            print("Time spent: ",timeDiff)            
            print("---------------------------")
            checkin = False
            checkout = False          
               
        #print("Row: :",item_count,row)   
        item_count += 1
        
       
conn.commit()
 
