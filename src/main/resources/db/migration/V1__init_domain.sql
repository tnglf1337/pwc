create table laufzeit_event(
    id serial primary key,
    laufzeit int,
    stamped_at timestamp,
    tarif_kosten float,
    kwh float
);

create table tarif(
    id serial primary key ,
    tarif_kosten float,
    stand date
);