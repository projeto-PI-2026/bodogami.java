
const botaoAbrir = document.querySelector('.botao-adicionar-jogo');
const modal = document.getElementById('modal-jogo');
const botaoFechar = document.querySelector('.botao-fechar');

botaoAbrir.addEventListener('click', () => {
  modal.classList.add('ativo');
});

botaoFechar.addEventListener('click', () => {
  modal.classList.remove('ativo');
});

window.addEventListener('click', (event) => {
  if (event.target === modal) {
    modal.classList.remove('ativo');
  }
});