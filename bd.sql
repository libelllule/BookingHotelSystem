select * from hotels
select * from bookings
delete from bookings
ALTER TABLE hotels 
ADD COLUMN rooms_total INTEGER;
UPDATE hotels 
SET rooms_total = rooms_available;
UPDATE hotels SET rooms_available = 2  WHERE id=4
UPDATE hotels 
SET rooms_total = rooms_available;
UPDATE hotels SET rooms_available = 1  WHERE id=1