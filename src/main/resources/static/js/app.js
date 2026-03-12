(() => {
  const nav = document.querySelector("[data-nav]");
  const toggle = document.querySelector("[data-nav-toggle]");

  if (!nav || !toggle) return;

  const setOpen = (open) => {
    nav.dataset.open = String(open);
    toggle.setAttribute("aria-expanded", String(open));
  };

  const isOpen = () => nav.dataset.open === "true";

  setOpen(false);

  toggle.addEventListener("click", () => {
    setOpen(!isOpen());
  });

  // Fecha ao clicar em um link (mobile)
  nav.addEventListener("click", (e) => {
    const link = e.target.closest("a");
    if (!link) return;
    setOpen(false);
  });

  // Fecha ao clicar fora
  document.addEventListener("click", (e) => {
    if (!isOpen()) return;
    const clickedInside = nav.contains(e.target) || toggle.contains(e.target);
    if (!clickedInside) setOpen(false);
  });

  // Fecha com ESC
  document.addEventListener("keydown", (e) => {
    if (e.key !== "Escape") return;
    if (!isOpen()) return;
    setOpen(false);
    toggle.focus();
  });

  // Se a pessoa redimensionar pra desktop, garante que não fica preso "open"
  window.addEventListener("resize", () => {
    if (window.matchMedia("(max-width: 720px)").matches) return;
    setOpen(false);
  });
})();

(() => {
  const cards = document.querySelectorAll("[data-flip-card]");
  if (!cards.length) return;

  const setFlipped = (card, flipped) => {
    card.dataset.flipped = String(flipped);
    card.setAttribute("aria-pressed", String(flipped));
  };

  for (const card of cards) {
    setFlipped(card, false);

    card.addEventListener("click", () => {
      const flipped = card.dataset.flipped === "true";
      setFlipped(card, !flipped);
    });

    card.addEventListener("keydown", (e) => {
      if (e.key !== "Enter" && e.key !== " ") return;
      e.preventDefault();
      const flipped = card.dataset.flipped === "true";
      setFlipped(card, !flipped);
    });
  }
})();

