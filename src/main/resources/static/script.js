let level = 1;

function setLevel(l) {
  level = l;
}

function explain() {
  const question = document.getElementById("question").value;
  const output = document.getElementById("output");

  fetch("/api/explain", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify({
      level: level,
      question: question
    })
  })
    .then(response => {
      if (response.status === 429) {
        alert("You're sending requests too fast. Try again in a minute.");
        throw new Error("Rate limited");
      }
      return response.text();
    })
    .then(data => {
      output.innerText = data;
    })
    .catch(err => {
      console.error(err);
    });
}
