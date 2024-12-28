const amqp = require('amqplib');
const fs = require('fs');
const cron = require('node-cron');

const EXCHANGE = 'rabbit.care.direct';
const ROUTING_KEY = 'rabbitCareKey';
const QUEUE = 'rabbit.care.queue';

let locations = [];
let currentIndex = 0;

// Učitaj JSON fajl
fs.readFile('locations.json', 'utf8', (err, data) => {
  if (err) {
    console.error('Greška pri čitanju fajla:', err);
    return;
  }
  locations = JSON.parse(data);  // Učitaj JSON podatke
});

// Funkcija koja šalje poruku u RabbitMQ
const sendMessage = async (location) => {
  try {
    // Poveži se sa RabbitMQ
    const connection = await amqp.connect('amqp://localhost');
    const channel = await connection.createChannel();

    // Postavi exchange, queue i bind
    await channel.assertExchange(EXCHANGE, 'direct', { durable: true });
    await channel.assertQueue(QUEUE, { durable: true });
    await channel.bindQueue(QUEUE, EXCHANGE, ROUTING_KEY);

    const message = JSON.stringify(location);  // Poruka u JSON formatu
    channel.sendToQueue(QUEUE, Buffer.from(message), {
      persistent: true,
      contentType: 'application/json' // Explicitly set content-type as JSON
    });
    console.log(`Poslata poruka: ${JSON.stringify(location)}`);

    // Zatvori kanal i konekciju
    setTimeout(() => {
      channel.close();
      connection.close();
    }, 500);
  } catch (error) {
    console.error('Greška pri slanju poruke:', error);
  }
};

// Cron job: svakih 10 minuta pošaljemo jednu lokaciju
cron.schedule('* * * * *', () => {
  if (locations.length > 0 && currentIndex < locations.length) {
    const location = locations[currentIndex];
    sendMessage(location);  // Pošaljemo trenutnu lokaciju
    currentIndex = (currentIndex + 1) % locations.length;  // Povećavamo index
  } else {
    console.log('Nema više lokacija za slanje.');
  }
});

console.log('Cron job pokrenut. Svakih 10 minuta šaljemo po jednu lokaciju.');
